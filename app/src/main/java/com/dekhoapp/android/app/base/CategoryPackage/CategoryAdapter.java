package com.dekhoapp.android.app.base.CategoryPackage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dekhoapp.android.app.base.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> implements View.OnClickListener {

    private List<CategoryClass> categoryList;
    private Context mcontext;
    float scale;
    int w,h;

    public CategoryAdapter(Context c,List<CategoryClass> catList)
    {
        this.categoryList = catList;
        this.mcontext=c;
        scale=mcontext.getResources().getDisplayMetrics().density;
        w=mcontext.getResources().getDisplayMetrics().widthPixels-(int)(14*scale);
        h=(w/16)*9;

    }

    @Override
    public void onClick(View v) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView catimage;
        public TextView catname;

        public MyViewHolder(View view) {
            super(view);
            catimage = (ImageView)view.findViewById(R.id.catimage);
            catname = (TextView)view.findViewById(R.id.catname);

            view.setOnClickListener(CategoryAdapter.this);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);

        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CategoryClass cat = categoryList.get(position);
        holder.catname.setText(cat.getCategory_name());

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            holder.catimage.setImageResource(cat.getImage());
        }
        else
        {
            Bitmap bitmap = BitmapFactory.decodeResource(mcontext.getResources(),categoryList.get(position).getImage());
            bitmap=Bitmap.createScaledBitmap(bitmap,w,h,false);

            bitmap=imagehelper.getRoundedCornerBitmap(mcontext,bitmap,10,w,h,true,true,false,false);
            holder.catimage.setImageBitmap(bitmap);

        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
