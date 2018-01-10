package qlyh8.tistory.com.calculator;

import android.annotation.SuppressLint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private EditText editText1, editText2;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    // 초기화
    public void init(){
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        textResult = findViewById(R.id.textResult);

        // EditText 입력 키보드 감추기
        editText1.setInputType(InputType.TYPE_NULL);
        editText2.setInputType(InputType.TYPE_NULL);

        editText1.setText("");
        editText2.setText("");
        textResult.setText("");

        // "C" 버튼을 길게 눌렀을 때 이벤트
        findViewById(R.id.btnClear).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(editText1.isFocused())
                    editText1.setText("");
                else if(editText2.isFocused())
                    editText2.setText("");
                return true;
            }
        });
    }

    // "C" 버튼 클릭 시 이벤트
    public void clickBtnClear(View view) {
        String str;

        if(editText1.isFocused()){
            str = editText1.getText().toString();

            if (!str.equals("")) {
                if(str.length() == 1)
                    editText1.setText("");
                else
                    editText1.setText(str.substring(0, str.length() - 1));
            }
        }
        else if(editText2.isFocused()){
            str = editText2.getText().toString();
            if (!str.equals("")){
                if(str.length() == 1)
                    editText2.setText("");
                else
                    editText2.setText(str.substring(0, str.length() - 1));
            }
        }
    }

    // 위 버튼 클릭 시 이벤트
    public void clickBtnUp(View view) {
        editText1.requestFocus();
        editText2.clearFocus();
        editText1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAqua1));
        editText2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorMediumGray1));
    }

    // 아래 버튼 클릭 시 이벤트
    public void clickBtnDown(View view) {
        editText1.clearFocus();
        editText2.requestFocus();
        editText1.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorMediumGray1));
        editText2.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorAqua1));
    }

    // "=" 버튼 클릭 시 이벤트
    @SuppressLint("SetTextI18n")
    public void clickBtnResult(View view) {
        if(editText1.getText().toString().equals("") || editText2.getText().toString().equals(""))
            textResult.setText("");
        else if(editText2.getText().toString().equals("0"))
            textResult.setText("0으로 나눌 수 없습니다.");
        else{
            try {
                if(Float.parseFloat(editText1.getText().toString()) %
                        Float.parseFloat(editText2.getText().toString()) == 0){
                    int result = Integer.parseInt(editText1.getText().toString()) /
                            Integer.parseInt(editText2.getText().toString());
                    textResult.setText(""+result);
                }
                else{
                    float result = Float.parseFloat(editText1.getText().toString()) /
                        Float.parseFloat(editText2.getText().toString());
                    textResult.setText(""+result);
                }
            } catch (Exception e){
                textResult.setText("");
            }
        }
    }

    // "1" 버튼 클릭 시 이벤트
    public void clickBtn1(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("1");
            else if(editText1.getText().toString().equals("0"))
                editText1.setText("1");
            else
                editText1.getText().append("1");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("1");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("1");
            else
                editText2.getText().append("1");
        }
    }

    // "2" 버튼 클릭 시 이벤트
    public void clickBtn2(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("2");
            else if(editText1.getText().toString().equals("0"))
                editText1.setText("2");
            else
                editText1.getText().append("2");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("2");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("2");
            else
                editText2.getText().append("2");
        }
    }

    // "3" 버튼 클릭 시 이벤트
    public void clickBtn3(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("3");
            else if(editText1.getText().toString().equals("0"))
                editText1.setText("3");
            else
                editText1.getText().append("3");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("3");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("3");
            else
                editText2.getText().append("3");
        }
    }

    // "4" 버튼 클릭 시 이벤트
    public void clickBtn4(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("4");
            else if(editText1.getText().toString().equals("0"))
                editText1.setText("4");
            else
                editText1.getText().append("4");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("4");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("4");
            else
                editText2.getText().append("4");
        }
    }

    // "5" 버튼 클릭 시 이벤트
    public void clickBtn5(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("5");
            else if(editText1.getText().toString().equals("0"))
                editText1.setText("5");
            else
                editText1.getText().append("5");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("5");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("5");
            else
                editText2.getText().append("5");
        }
    }

    // "6" 버튼 클릭 시 이벤트
    public void clickBtn6(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("6");
            else if(editText1.getText().toString().equals("0"))
                editText1.setText("6");
            else
                editText1.getText().append("6");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("6");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("6");
            else
                editText2.getText().append("6");
        }
    }

    // "7" 버튼 클릭 시 이벤트
    public void clickBtn7(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("7");
            else if(editText1.getText().toString().equals("0"))
                editText1.setText("7");
            else
                editText1.getText().append("7");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("7");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("7");
            else
                editText2.getText().append("7");
        }
    }

    // "8" 버튼 클릭 시 이벤트
    public void clickBtn8(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("8");
            else if(editText1.getText().toString().equals("0"))
                editText1.setText("8");
            else
                editText1.getText().append("8");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("8");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("8");
            else
                editText2.getText().append("8");
        }
    }

    // "9" 버튼 클릭 시 이벤트
    public void clickBtn9(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("9");
            else if(editText1.getText().toString().equals("0"))
                editText1.setText("9");
            else
                editText1.getText().append("9");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("9");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("9");
            else
                editText2.getText().append("9");
        }
    }

    // "0" 버튼 클릭 시 이벤트
    public void clickBtn0(View view) {
        if(editText1.isFocused()){
            if(editText1.getText().toString().equals(""))
                editText1.setText("0");
            else if(editText1.getText().toString().equals("00"))
                editText1.setText("0");
            else
                editText1.getText().append("0");
        }
        else if(editText2.isFocused()){
            if(editText2.getText().toString().equals(""))
                editText2.setText("0");
            else if(editText2.getText().toString().equals("0"))
                editText2.setText("0");
            else
                editText2.getText().append("0");
        }
    }
}
