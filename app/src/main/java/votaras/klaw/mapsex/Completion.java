package votaras.klaw.mapsex;

import android.content.Intent;
import android.os.Bundle;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Completion extends AppCompatActivity {

    private EditText CompTime, CompKM, CompPercent;
    private Button btn_comp_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion);
    };

    public void onHomeClick(View view){
        // действия, совершаемые после нажатия на кнопку
        // Создаем объект Intent для вызова новой Activity
        Intent intent = new Intent(this, MainActivity.class);
        EditText CompTime = (EditText)findViewById(R.id.txtCompTime);
        EditText CompKM = (EditText)findViewById(R.id.txtCompKM);
        EditText CompPercent = (EditText)findViewById(R.id.txtCompPercent);

        //передаем значения в объект intent

        // запуск activity
        startActivity(intent);
    };
}