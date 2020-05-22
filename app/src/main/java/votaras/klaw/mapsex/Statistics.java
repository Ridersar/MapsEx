package votaras.klaw.mapsex;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Statistics extends AppCompatActivity {

    private EditText Statistics;
    private Button btn_statistics, btn_stat_back;;
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        //addListenerOnButton ();

        //создание / открытие базы данных
        myDB =
                openOrCreateDatabase("my.db", MODE_PRIVATE, null);

        //создание базы данных
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS user (numberOfRoutes INT, flag INT)"
        );

        //запрос
        Cursor myCursor =
                myDB.rawQuery("select numberOfRoutes, flag from user", null);

        //проход по БД
        int num = 0; //кол-во построенных маршрутов
        while(myCursor.moveToNext())
        {
            num = myCursor.getInt(0);
        }
        String Snum = Integer.toString(num);


        EditText Statistics = (EditText)findViewById(R.id.txtStatistics);
        Statistics.setText(Snum);
    };

    public void onButtonClick(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        EditText Statistics = (EditText)findViewById(R.id.txtStatistics);
        startActivity(intent);
    };
/*
    public void addListenerOnButton () {
        btn_stat_back = (Button) findViewById(R.id.btnBack);
        btn_stat_back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(".MainActivity");
                        startActivity(intent);
                    }
                }
        );
    }
    */
}