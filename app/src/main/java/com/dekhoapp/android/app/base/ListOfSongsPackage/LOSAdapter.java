package com.dekhoapp.android.app.base.ListOfSongsPackage;

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

import com.dekhoapp.android.app.base.CategoryPackage.imagehelper;
import com.dekhoapp.android.app.base.R;

import java.util.List;

public class LOSAdapter extends RecyclerView.Adapter<LOSAdapter.MyViewHolder> implements View.OnClickListener {

    private List<LOSClass> LOSList;
    private Context mcontext;
    float scale;
    int w,h;

    public LOSAdapter(Context c,List<LOSClass> catList)
    {
        this.LOSList = catList;
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
            catimage = (ImageView)view.findViewById(R.id.songimage);
            catname = (TextView)view.findViewById(R.id.songname);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.los_row, parent, false);

        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LOSClass cat = LOSList.get(position);
        holder.catname.setText(cat.getname());

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            holder.catimage.setImageResource(cat.getSong_image());
        }
        else
        {
            Bitmap bitmap = BitmapFactory.decodeResource(mcontext.getResources(),LOSList.get(position).getSong_image());
            bitmap=Bitmap.createScaledBitmap(bitmap,w,h,false);

            bitmap= imagehelper.getRoundedCornerBitmap(mcontext,bitmap,10,w,h,true,true,false,false);
            holder.catimage.setImageBitmap(bitmap);

        }
    }

    @Override
    public int getItemCount() {
        return LOSList.size();
    }
}
