package com.oscasistemas.appgestionlogistica.listViews;

import java.util.List;



import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.articulo.MapeoArticulo;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArticleInfoItemAdapter extends BaseAdapter {

	private Context context;
	private List<MapeoArticulo> articles;
	LayoutInflater layoutInflater;
	
	
	public ArticleInfoItemAdapter(Context context, List<MapeoArticulo> items) {
        this.context = context;
        this.articles = items;
        LayoutInflater.from(context);
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.articles.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.articles.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View rowView = convertView;
		 
	        if (convertView == null) {
	            // Create a new view into the list.
	            LayoutInflater inflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            rowView = inflater.inflate(R.layout.article_list_item, parent, false);

	        }
	 
	        // Set data into the view.
	        TextView code = (TextView) rowView.findViewById(R.id.ArticleCode);
	        TextView name = (TextView) rowView.findViewById(R.id.ArticleName);
	        AutoResizeTextView alias = (AutoResizeTextView) rowView.findViewById(R.id.ArticleALIAS);
	 
	        MapeoArticulo article = this.articles.get(position);
	        code.setText(String.valueOf(article.getCode()));
	        alias.setText(String.valueOf(" "+article.getName()));
	        name.setText(String.valueOf(article.getDescription()));
	        return rowView;
	}

}
