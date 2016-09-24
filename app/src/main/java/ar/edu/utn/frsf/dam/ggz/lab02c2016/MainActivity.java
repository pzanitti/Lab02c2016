package ar.edu.utn.frsf.dam.ggz.lab02c2016;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static ar.edu.utn.frsf.dam.ggz.lab02c2016.R.string.must_select_item;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    DecimalFormat f = new DecimalFormat("##.00");

    ElementoMenu[] listaBebidas;
    ElementoMenu[] listaPlatos;
    ElementoMenu[] listaPostre;

    private ListView listView;
    private TextView displayOrderTextView;

    private ArrayAdapter<ElementoMenu> entreeListAdapter;
    private ArrayAdapter<ElementoMenu> dessertListAdapter;
    private ArrayAdapter<ElementoMenu> drinkListAdapter;

    final private Order order = new Order();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = (Button) findViewById(R.id.addButton);
        Button confirmOrderButton = (Button) findViewById(R.id.confirmOrderButton);
        Button restartOrderButton = (Button) findViewById(R.id.restartOrderButton);

        addButton.setOnClickListener(this);
        confirmOrderButton.setOnClickListener(this);
        restartOrderButton.setOnClickListener(this);

        iniciarListas();
        entreeListAdapter = new ArrayAdapter<ElementoMenu>(this, android.R.layout.simple_list_item_single_choice, listaPlatos);
        dessertListAdapter = new ArrayAdapter<ElementoMenu>(this, android.R.layout.simple_list_item_single_choice, listaPostre);
        drinkListAdapter = new ArrayAdapter<ElementoMenu>(this, android.R.layout.simple_list_item_single_choice, listaBebidas);

        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        displayOrderTextView = (TextView) findViewById(R.id.displayOrderTextView);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.addButton:
                if (order.isConfirmed()) {
                    Toast.makeText(MainActivity.this, R.string.order_is_closed, Toast.LENGTH_SHORT).show();
                    break;
                }

                int index = listView.getSelectedItemPosition();
                ElementoMenu selected = (ElementoMenu) listView.getItemAtPosition(listView.getCheckedItemPosition());
                if (selected == null) {
                    Toast.makeText(MainActivity.this, must_select_item, Toast.LENGTH_SHORT).show();
                    break;
                }
                order.add(selected);
                displayOrderTextView.setText(order.toString());
                break;

            case R.id.confirmOrderButton:
                if(order.isEmpty()) {
                    Toast.makeText(MainActivity.this, must_select_item, Toast.LENGTH_SHORT).show();
                    break;
                }
                order.setConfirmed(true);
                displayOrderTextView.setText(order.toString());
                break;

            case R.id.restartOrderButton:
                order.clear();
                displayOrderTextView.setText(order.toString());
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int id) {
        switch(id) {

            case R.id.entreeRadioButton:
                listView.setAdapter(entreeListAdapter);
                break;


            case R.id.dessertRadioButton:
                listView.setAdapter(dessertListAdapter);
                break;


            case R.id.drinkRadioButton:
                listView.setAdapter(drinkListAdapter);
                break;
        }

    }

    class ElementoMenu {
        private Integer id;
        private String nombre;
        private Double precio;

        public ElementoMenu() {
        }

        public ElementoMenu(Integer i, String n, Double p) {
            this.setId(i);
            this.setNombre(n);
            this.setPrecio(p);
        }

        public ElementoMenu(Integer i, String n) {
            this(i,n,0.0);
            Random r = new Random();
            this.precio= (r.nextInt(3)+1)*((r.nextDouble()*100));
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        @Override
        public String toString() {
            return this.nombre+ "( "+f.format(this.precio)+")";
        }
    }


    class Order {
        private boolean confirmed = false;
        final private ArrayList<ElementoMenu> contents = new ArrayList<ElementoMenu>();

        public boolean isConfirmed() {
            return confirmed;
        }

        public void setConfirmed(boolean confirmed)  {
            if(isEmpty() && confirmed) throw new IllegalStateException("Cannot confirm an empty order");
            this.confirmed = confirmed;
        }

        public boolean isEmpty() {
            return contents.isEmpty();
        }

        public void clear() {
            contents.clear();
            confirmed = false;
        }

        public void add(ElementoMenu item) {
            if(item == null) throw new NullPointerException("Argument cannot be null");
            if(confirmed) throw new IllegalStateException("Cannot add to a confirmed order");
            contents.add(item);
        }

        public double getTotalPrice() {
            double total = 0.0;
            for(ElementoMenu item : contents) {
                //NOTE: Summing doubles - when formatted as decimals, the totals may not add up!
                total += item.getPrecio();
            }
            return total;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for(ElementoMenu item : contents) {
                sb.append(item.toString()); sb.append('\n');
            }
            if(confirmed) {
                sb.append(R.string.total); sb.append(' '); sb.append(f.format(getTotalPrice()));
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    private void iniciarListas(){
        // inicia lista de bebidas
        listaBebidas = new ElementoMenu[7];
        listaBebidas[0]=new ElementoMenu(1,"Coca");
        listaBebidas[1]=new ElementoMenu(4,"Jugo");
        listaBebidas[2]=new ElementoMenu(6,"Agua");
        listaBebidas[3]=new ElementoMenu(8,"Soda");
        listaBebidas[4]=new ElementoMenu(9,"Fernet");
        listaBebidas[5]=new ElementoMenu(10,"Vino");
        listaBebidas[6]=new ElementoMenu(11,"Cerveza");
        // inicia lista de platos
        listaPlatos= new ElementoMenu[14];
        listaPlatos[0]=new ElementoMenu(1,"Ravioles");
        listaPlatos[1]=new ElementoMenu(2,"Gnocchi");
        listaPlatos[2]=new ElementoMenu(3,"Tallarines");
        listaPlatos[3]=new ElementoMenu(4,"Lomo");
        listaPlatos[4]=new ElementoMenu(5,"Entrecot");
        listaPlatos[5]=new ElementoMenu(6,"Pollo");
        listaPlatos[6]=new ElementoMenu(7,"Pechuga");
        listaPlatos[7]=new ElementoMenu(8,"Pizza");
        listaPlatos[8]=new ElementoMenu(9,"Empanadas");
        listaPlatos[9]=new ElementoMenu(10,"Milanesas");
        listaPlatos[10]=new ElementoMenu(11,"Picada 1");
        listaPlatos[11]=new ElementoMenu(12,"Picada 2");
        listaPlatos[12]=new ElementoMenu(13,"Hamburguesa");
        listaPlatos[13]=new ElementoMenu(14,"Calamares");
        // inicia lista de postres
        listaPostre= new ElementoMenu[15];
        listaPostre[0]=new ElementoMenu(1,"Helado");
        listaPostre[1]=new ElementoMenu(2,"Ensalada de Frutas");
        listaPostre[2]=new ElementoMenu(3,"Macedonia");
        listaPostre[3]=new ElementoMenu(4,"Brownie");
        listaPostre[4]=new ElementoMenu(5,"Cheescake");
        listaPostre[5]=new ElementoMenu(6,"Tiramisu");
        listaPostre[6]=new ElementoMenu(7,"Mousse");
        listaPostre[7]=new ElementoMenu(8,"Fondue");
        listaPostre[8]=new ElementoMenu(9,"Profiterol");
        listaPostre[9]=new ElementoMenu(10,"Selva Negra");
        listaPostre[10]=new ElementoMenu(11,"Lemon Pie");
        listaPostre[11]=new ElementoMenu(12,"KitKat");
        listaPostre[12]=new ElementoMenu(13,"IceCreamSandwich");
        listaPostre[13]=new ElementoMenu(14,"Frozen Yougurth");
        listaPostre[14]=new ElementoMenu(15,"Queso y Batata");
    }
}