package br.com.simcit.filmes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.simcit.filmes.dao.FilmeDao;
import br.com.simcit.filmes.model.Filme;

public class GerenciarFilmeActivity extends AppCompatActivity {

    private EditText edtTitulo;
    private EditText edtAno;
    private Spinner spGenero;
    private RadioGroup rgClassificacao;
    private RadioButton rbLivre;
    private RadioButton rb10Anos;
    private RadioButton rb14Anos;
    private RadioButton rb18Anos;
    private Filme filme;
    private boolean editando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_filme);

        //Obtendo os componentes que criamos no activity_gerenciar_filme.xml
        edtTitulo = (EditText) findViewById(R.id.edt_titulo);
        edtAno = (EditText) findViewById(R.id.edt_ano);
        spGenero = (Spinner) findViewById(R.id.sp_genero);
        rgClassificacao = (RadioGroup) findViewById(R.id.rg_classificacao);
        rbLivre = (RadioButton) findViewById(R.id.rb_livre);
        rb10Anos = (RadioButton) findViewById(R.id.rb_10anos);
        rb14Anos = (RadioButton) findViewById(R.id.rb_14anos);
        rb18Anos = (RadioButton) findViewById(R.id.rb_18anos);

        //Preenchemos o Spinner de Categorias
        preencherSpCategorias();

        //Verificamos se algum filme foi passado através de intent
        filme = (Filme) getIntent().getSerializableExtra("filme");
        //Se a activity recebeu um filme, nos vamos preencher os campos da activity
        // com os dados do filme recebido definimos que a opreção que estamos realizando
        // é uma edição, atraves da variavel editando
        if (filme != null) {
            preencherDados();
            editando = true;
        }
    }

    //Estamos inflando o arquivo menu_filme.xml no menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filme, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Verificamos o item de menu que foi selecionado
        if (item.getItemId() == R.id.action_salvar) {
            //Criamos o obejeto dao, para fazer as operações no banco de dados
            FilmeDao dao = new FilmeDao(this);
            //carregamos os dados dos campos da activity dentro do objeto filme
            carregarFilme();

            //Verificamos se a operação que estivermos realizando for uma edição ou uma inclusão
            if (editando) {
                //Pedimos ao objeto dao para alterar os dados do filme
                dao.alterar(filme);
                //Exibimos uma mensagem toast informado que o filme foi alterado.
                Toast.makeText(this, "Filme Alterado com Sucesso", Toast.LENGTH_SHORT).show();
            } else {
                //Pedimos ao objeto dao para inserir um novo filme
                dao.inserir(filme);
                //Exibimos uma mensagem toast informado que um novo filme foi inserido.
                Toast.makeText(this, "Filme Salvo com Sucesso", Toast.LENGTH_SHORT).show();
            }
            //Ao finalizar as operações de insersão ou edição finalizamos a activity
            //e retornamos para a tela anterior;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void preencherDados() {
        edtTitulo.setText(filme.getTitulo());
        edtAno.setText(String.valueOf(filme.getAno()));
        spGenero.setSelection(filme.getCategoria());
        switch (filme.getClassificacao()) {
            case "Livre":
                rbLivre.setChecked(true);
                break;
            case "10Anos":
                rb10Anos.setChecked(true);
                break;
            case "14Anos":
                rb14Anos.setChecked(true);
                break;
            case "18Anos":
                rb18Anos.setChecked(true);
                break;
        }
    }

    //Método responsável por preencher o Spinner de Categorias
    private void preencherSpCategorias() {
        //Cria uma lista contendo as categorias dos filmes
        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("Ação");
        categorias.add("Aventura");
        categorias.add("Comédia");
        categorias.add("Documentário");
        categorias.add("Drama");
        categorias.add("Infantil");
        categorias.add("Suspense");
        categorias.add("Terror");

        //Criamos um adaptador, que será responsável por exibir as categorias no spinner
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias);
        //Definimos uma animação ao exibir a lista
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Agora setamos o adapter no spinner
        spGenero.setAdapter(adapter);
    }

    private void carregarFilme() {
        //Verificamos se a operação não é uma edição, caso não seja criamos um novo objeto filme,
        //Caso seja, preenchemos com os dados que foram informados pelo usuário
        if (!editando) {
            filme = new Filme();
        }
        filme.setTitulo(edtTitulo.getText().toString());
        filme.setAno(Integer.parseInt(edtAno.getText().toString()));
        filme.setCategoria(spGenero.getSelectedItemPosition());
        //pegarmos do radio group, qual o id do botão que foi selecionado e definimos a classificação
        switch (rgClassificacao.getCheckedRadioButtonId()) {
            case R.id.rb_livre:
                filme.setClassificacao("Livre");
                break;
            case R.id.rb_10anos:
                filme.setClassificacao("10Anos");
                break;
            case R.id.rb_14anos:
                filme.setClassificacao("14Anos");
                break;
            case R.id.rb_18anos:
                filme.setClassificacao("18Anos");
                break;
        }
    }


}
