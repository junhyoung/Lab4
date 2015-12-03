        package github.com.junhyoung.lab4;
        import android.app.Activity;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.SimpleCursorAdapter;
        import android.widget.Toast;

        import java.util.ArrayList;

public class MainActivity extends Activity {

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "idList.db"; // name of Database;
    String tableName = "idListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;


    Cursor cursor;
    SimpleCursorAdapter adapter;

    // layout object
    EditText mEtName;
    EditText mEtName1;
    EditText mEtId;
    Button mBtInsert;
    Button mBtRead;
    Button mBtsort;
    Button mBtupdate;
    Button mBtdelete;
    static Boolean isasc = true;



    ListView mList;
    ArrayAdapter<String> baseAdapter;
    ArrayList<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // // Database 생성 및 열기
        db = openOrCreateDatabase(dbName,dbMode,null);
        // 테이블 생성
        createTable();

        mEtName = (EditText) findViewById(R.id.et_text);
        mEtName1=(EditText)findViewById(R.id.et_text2);
        mEtId=(EditText)findViewById(R.id.et_id);
        mBtInsert = (Button) findViewById(R.id.bt_insert);
        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtupdate=(Button) findViewById(R.id.bt_update);
        mBtdelete=(Button)findViewById(R.id.bt_delete);
        mBtsort=(Button) findViewById(R.id.bt_sort);
        ListView mList = (ListView) findViewById(R.id.list_view);

        mBtInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                insertData(name);
            }
        });

        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });
        //delete
        mBtdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = mEtId.getText().toString();
                int temp=Integer.parseInt(num);
                removeData(temp);
            }
        });
        //update
        mBtupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=mEtId.getText().toString();
                String name=mEtName1.getText().toString();
                try{
                    updateData(Integer.parseInt(num),name);
                }catch (Exception e){

                }
            }
        });

        mBtsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameList.clear();
                Sort();
                baseAdapter.notifyDataSetChanged();;
            }
        });

        // Create listview
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);

    }

    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " + "name text not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite","error: "+ e);
        }
    }

    // Table 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }
    // Data 정렬
    public void Sort(){
        Cursor results;
        if(isasc){
            String sql = "select * from " + tableName + " order by id " + "DESC ;";
            results = db.rawQuery(sql, null);
            // db.query("idListTable",new String[]{"id","name"},null,null,null,null,"id desc");
            isasc = false;
        } else {
            String sql = "select * from " + tableName + " order by id " + "asc ;";
            results = db.rawQuery(sql, null);
            //db.query("idListTable",new String[]{"id","name"},null,null,null,null,"id asc");
            isasc=true;
        }

        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
            //            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(name);
            results.moveToNext();
        }
        results.close();

    }

    // Data 추가
    public void insertData(String name) {
        String sql = "insert into " + tableName + " values(NULL, '" + name + "');";
        db.execSQL(sql);
    }

    // Data 업데이트
    public void updateData(int index, String name) {
        String sql = "update " + tableName + " set name = '" + name + "' where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 삭제
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 읽기(꺼내오기)
    public void selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(1);
            //            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");
        }
        result.close();
    }

    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);
            //            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(name);
            results.moveToNext();
        }
        results.close();
    }

}



