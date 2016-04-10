package br.com.marcogorak.exemploedittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    //Declarando atributos da classe
    EditText mEdtNome;
    EditText mEdtEmail;
    EditText mEdtSenha;
    EditText edtCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Associando os elementos da tela com os atributos da classe
        mEdtEmail = (EditText)findViewById(R.id.edtNome);
        mEdtEmail = (EditText)findViewById(R.id.edtEmail);
        mEdtSenha = (EditText)findViewById(R.id.edtSenha);
        mEdtSenha.setOnEditorActionListener(this);
        edtCep = (EditText)findViewById(R.id.edtCep);

        edtCep.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {

                //Quando o texto é alterado o onTextChanged é chamado
                //Essa flag evita a chamada infinita desse método
                if(isUpdating) {
                    isUpdating = false;
                    return;
                }

                // Ao apagar o texto, a máscara é removida, então o posicionamento do cursor precisa
                //saber se o texto atual tinha ou não, máscara
                boolean hasMask = s.toString().indexOf('.') > -1 || s.toString().indexOf('-')> -1;

                //Remove o '.' e '-' da String
                String str = s.toString().replaceAll("[.]", "").replaceAll("[-]","");

                //Os parâmetros before e after dizem o tamanho anterior e atual da String digitada,
                //se after > before é porque está digitandom caso contrário, está apagando
                if (after > before) {
                    //Se tem mais de 5 caracteres (sem máscara) coloca o '.' e o '-'
                    if(str.length() > 5) {
                        str= str.substring(0, 2) + '.' + str.substring(2,5) + '-' + str.substring(5);
                        //Se tem mais de 2, coloca só o ponto
                    } else if(str.length() > 2) {
                        str = str.substring(0,2) + '.' + str.substring(2);
                    }
                    //Seta a flag para evitar chamada infinta
                    isUpdating = true;
                    //Seta o novo texto
                    edtCep.setText(str);
                    //Seta a posição do cursor
                    edtCep.setSelection(edtCep.getText().length());
                } else {
                    isUpdating = true;

                    edtCep.setText(str);
                    //Se estiver apagado posiciona o cursos no local correto. Isso trata a deleção
                    //dos caracteres da máscara.
                    edtCep.setSelection
                            (Math.max(0, Math.min(hasMask ? start - before : start, str.length())));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v == mEdtSenha && EditorInfo.IME_ACTION_DONE == actionId) {
            String nome  = mEdtNome.getText().toString();
            String email = mEdtEmail.getText().toString();
            String senha = mEdtSenha.getText().toString();
            boolean ok = true;

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ok = false;
            }
            if(!senha.equals("123")) {
                mEdtSenha.setError(getString(R.string.msg_erro_senha));
                ok = false;
            }
            if (ok) {
                Toast.makeText
                        (this, getString
                                (R.string.msg_sucesso, nome, email), Toast.LENGTH_SHORT).show();
            }
            return true; //remover esse return para fechar o teclado virutal
        }

        return false;
    }
}
