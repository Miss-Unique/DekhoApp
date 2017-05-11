package com.dekhoapp.android.app.base.CategoryPackage;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dekhoapp.android.app.base.ListOfSongsPackage.List_of_Songs;
import com.dekhoapp.android.app.base.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.animation;

public class Category extends AppCompatActivity {

    private List<CategoryClass> categoryList = new ArrayList<>();
    private List<CategoryClass> m = new ArrayList<>();
    public List<CategoryClass> filtered = new ArrayList<>();
    public EmptyRecyclerView recyclerView;
    private CategoryAdapter mAdapter;
    private EditText edit;
    //GridLayoutManager mLayoutManager;
    DatabaseReference mDatabase,db;
    private static final int TOTAL_ITEM_EACH_LOAD = 7;
    private int currentPage = 0;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = (EmptyRecyclerView) findViewById(R.id.rv_category);
        edit=(EditText)findViewById(R.id.edit);

        LinearLayout emptyView = (LinearLayout) findViewById(R.id.todo_list_empty_view);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration(2000);
        animation.setInterpolator (new DecelerateInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.start ();
        recyclerView.setEmptyView(emptyView);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //set adapter
        addingrows(categoryList);
        m=categoryList;

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) { // when we have reached end of RecyclerView this event fired
                loadMoreData();
            }
        });
        loadData();

        //preparecategoryData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                CategoryClass category = m.get(position);
                Intent intent=new Intent(Category.this,List_of_Songs.class);
                intent.putExtra("Name",category.getCategory_name());
                Toast.makeText(Category.this, category.getCategory_name(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        //search functionality
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(edit.getText().equals(""))
                {
                    addingrows(categoryList);
                }
                else
                {
                    filtered.clear();
                    addingrows(filtered);

                    s = s.toString().toLowerCase();
                    for (int i = 0; i < categoryList.size(); i++) {
                        String item = categoryList.get(i).getCategory_name().toString().toLowerCase();

                        if (item.contains(s)) {
                            filtered.add(categoryList.get(i));
                        }
                    }

                    m = filtered;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addingrows(final List<CategoryClass> k)
    {
        mAdapter = new CategoryAdapter(getApplicationContext(),k);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //mLayoutManager=new GridLayoutManager(Category.this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
    private void loadData() {
        // example
        // at first load : currentPage = 0 -> we startAt(0 * 10 = 0)
        // at second load (first loadmore) : currentPage = 1 -> we startAt(1 * 10 = 10)
        db = mDatabase.child("Soumya").child("Music").getRef();

        db.limitToFirst(TOTAL_ITEM_EACH_LOAD)
                .startAt(currentPage*TOTAL_ITEM_EACH_LOAD)
                .orderByChild("number").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if(!dataSnapshot.hasChildren())
                {
                    Toast.makeText(Category.this, "No more Categories", Toast.LENGTH_SHORT).show();
                    currentPage--;
                }

                String category = dataSnapshot.getKey();

                    CategoryClass ct = new CategoryClass(R.drawable.song,category);
                    categoryList.add(ct);
                    mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadMoreData(){
        currentPage++;
        loadData();
    }

    private void preparecategoryData()
    {
        db = mDatabase.child("Soumya").child("Music").getRef();

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                String category = dataSnapshot.getKey();

                CategoryClass ct = new CategoryClass(R.drawable.song,category);
                categoryList.add(ct);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}