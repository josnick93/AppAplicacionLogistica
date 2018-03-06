package com.oscasistemas.appgestionlogistica.BusquedaArticulos;



import java.util.List;
import com.MapeoBD.Articulo.MapeoArticulo;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorListaArticulos extends BaseAdapter {

	private Context context;
	private List<MapeoArticulo> articles;
	LayoutInflater layoutInflater;
	
	
	public AdaptadorListaArticulos(Context context, List<MapeoArticulo> items) {
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
	        code.setText(AuxiliarFunctions.format(article.getArticulo(),"xx.xxx.xxxx"));
	        alias.setText(" "+article.getAlias());
	        name.setText(article.getDescripcion());
	        return rowView;
	}

}
