package hu.xyzor.practise.todoapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Todo lista adapter létrehozása és feltöltése teszt adatokkal
        ArrayList<Todo> todos = new ArrayList<Todo>();
        todos.add(new Todo("title1",Todo.Priority.LOW, "2012.09.26.","description1"));
        todos.add(new Todo("title2",Todo.Priority.MEDIUM, "2012.09.27.","description2"));
        todos.add(new Todo("title3",Todo.Priority.HIGH, "2012.09.28.","description3"));

        TodoAdapter todoAdapter = new TodoAdapter(getApplicationContext(), todos);
        setListAdapter(todoAdapter);

        //A létrehozott view(kép + cím + dátum) kattintási eseményének meghívásakor leírás megjelenítése
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Todo selectedTodo = (Todo) getListAdapter().getItem(position);
                Toast.makeText(MainActivity.this, selectedTodo.getDescription(), Toast.LENGTH_LONG).show();
            }
        });

        //ListActivity-t használunk, aminek meghívhatjuk a 'getListView()' fgv-ét
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        //ha a listára kattintva jön létre a ContextMenu
        if(v.equals(getListView())) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) menuInfo;

            //menu címe a hívó lista elem lesz
            menu.setHeaderTitle(
                    ((Todo) getListAdapter().getItem(info.position)).getTitle());
            //a menüt egy .xml forrásfájlban állítjuk össze
            String[] menuItems = getResources().getStringArray(R.array.todomenu);

            for(int i=0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if(item.getItemId() == 0) { //Törlés
            //az adapterből törlöm az elemet, majd a View-t frissítem
            ((TodoAdapter)getListAdapter()).deleteRow(
                    (Todo)getListAdapter().getItem(info.position));

            //View frissítés
            ((TodoAdapter)getListAdapter()).notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.itemCreateTodo) {
            Intent myIntent = new Intent();
            myIntent.setClass(this, ActivityCreateTodo.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //(Kerülendő!!)
        if(DataPreferences.todoToCreate != null) {
            ((TodoAdapter)getListAdapter()).addItem(DataPreferences.todoToCreate);
            DataPreferences.todoToCreate = null;
            ((TodoAdapter) getListAdapter()).notifyDataSetChanged();
        }
    }
}