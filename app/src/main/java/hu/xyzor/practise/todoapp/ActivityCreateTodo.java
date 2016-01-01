package hu.xyzor.practise.todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Xyzor on 2016.01.01..
 */
public class ActivityCreateTodo extends Activity {

    private static final int DATE_DUE_DIALOG_ID = 1;
    private Calendar calSelectedDate = Calendar.getInstance();
    private EditText editTodoTitle;
    private Spinner spnrTodoPriority;
    private TextView txtDueDate;
    private EditText editTodoDesciption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.createtodo);

        //Dátum inicializálás
        calSelectedDate.setTime(new Date(System.currentTimeMillis()));

        // UI elemek
        editTodoTitle = (EditText) findViewById(R.id.todoTitle);
        spnrTodoPriority = (Spinner) findViewById(R.id.todoPriority);
        String[] priorities = new String[] {
                "Low",
                "Medium",
                "High"
        };
        spnrTodoPriority.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities));
        txtDueDate = (TextView) findViewById(R.id.todoDueDate);
        refreshDataText();
        txtDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DUE_DIALOG_ID);
            }
        });
        editTodoDesciption = (EditText) findViewById(R.id.todoDescription);

        Button btnOk = (Button) findViewById(R.id.btnCreateTodo);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo.Priority selPrior = Todo.Priority.LOW;
                switch(spnrTodoPriority.getSelectedItemPosition()) {
                    case 0:
                        selPrior = Todo.Priority.LOW;
                        break;
                    case 1:
                        selPrior = Todo.Priority.LOW;
                        break;
                    case 2:
                        selPrior = Todo.Priority.HIGH;
                        break;
                    default:
                        selPrior = Todo.Priority.HIGH;
                        break;
                }
                //Fragment-ek hiányában ezt a megoldást alkalmazzuk (Kerülendő!!)
                DataPreferences.todoToCreate =
                        new Todo(
                            editTodoTitle.getText().toString(),
                            selPrior,
                            txtDueDate.getText().toString(),
                            editTodoDesciption.getText().toString()
                        );
                finish();


            }
        });
        Button btnCancel = (Button) findViewById(R.id.btnCancelCreateTodo);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPreferences.todoToCreate = null;
                finish();
            }
        });

    }

    private void refreshDataText() {
        StringBuilder sb = new StringBuilder();
        sb.append(calSelectedDate.get(Calendar.YEAR));
        sb.append(". ");
        sb.append(calSelectedDate.get(Calendar.MONTH)+1);
        sb.append(". ");
        sb.append(calSelectedDate.get(Calendar.DAY_OF_MONTH));
        sb.append(". ");

        txtDueDate.setText(sb.toString());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_DUE_DIALOG_ID:
                return new DatePickerDialog(
                        this,
                        mDateSetListener,
                        calSelectedDate.get(Calendar.YEAR),
                        calSelectedDate.get(Calendar.MONTH),
                        calSelectedDate.get(Calendar.DAY_OF_MONTH)
                );
            default:
                return null;
        }
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calSelectedDate.set(Calendar.YEAR, year);
            calSelectedDate.set(Calendar.MONTH, monthOfYear+1);
            calSelectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            refreshDataText();
            removeDialog(DATE_DUE_DIALOG_ID );
        }
    };
}
