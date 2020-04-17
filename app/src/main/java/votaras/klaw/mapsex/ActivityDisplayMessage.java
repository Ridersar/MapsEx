package votaras.klaw.mapsex;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityDisplayMessage extends AppCompatActivity {

    private EditText A, B, KM, Time;
    private Button btn_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
    };
/*
    public void addListenerOnButton () {
        btn_go = (Button)findViewById(R.id.btnGO);
        btn_go.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(".activity_go_route");
                        startActivity(intent);
                    }
                }
        );
    };
*/

    public void onButtonClick(View v){
        EditText A = (EditText)findViewById(R.id.txtA);
        EditText B = (EditText)findViewById(R.id.txtB);
        EditText KM = (EditText)findViewById(R.id.txtKM);
        EditText Time = (EditText)findViewById(R.id.txtTime);
    };

    // Метод обработки нажатия на кнопку
    public void sendMessage(View view) {
        // действия, совершаемые после нажатия на кнопку
        // Создаем объект Intent для вызова новой Activity
        Intent intent = new Intent(this, GoRoute.class);

        // запуск activity
        startActivity(intent);

    }

}