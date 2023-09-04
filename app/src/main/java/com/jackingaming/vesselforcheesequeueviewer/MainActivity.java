package com.jackingaming.vesselforcheesequeueviewer;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jackingaming.vesselforcheesequeueviewer.order.DataStore;
import com.jackingaming.vesselforcheesequeueviewer.order.LocalDateTimeTypeAdapter;
import com.jackingaming.vesselforcheesequeueviewer.order.MenuItemInfo;
import com.jackingaming.vesselforcheesequeueviewer.order.MenuItemInfoAdapter;
import com.jackingaming.vesselforcheesequeueviewer.order.MenuItemInfoListWrapper;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    //    public static final String URL_ADD_NEW_MEAL = "http://192.168.1.143:8080/meals/add";
//    public static final String URL_GET_ALL_MEALS = "http://192.168.1.143:8080/meals/all";
//    public static final String URL_DELETE_MEAL_BY_ID = "http://192.168.1.143:8080/meals/delete";
    public static final String URL_GET_ALL_ORDERS = "http://192.168.1.143:8080/orders/all";

    private RequestQueue requestQueue;
    private Toolbar toolbar;
    private MenuItem actionMenuItem;

    //    private List<Meal> localData;
//    private MealAdapter mealAdapter;
    private List<MenuItemInfo> menuItemInfos;
    private MenuItemInfoAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView recyclerView;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private ScheduledFuture requestRepeatingGetAllMeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.fragment_meal_list);
        requestQueue = Volley.newRequestQueue(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        localData = new ArrayList<Meal>();
//        MealAdapter.ItemClickListener itemClickListener = new MealAdapter.ItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Log.i(TAG, "MealAdapter.ItemClickListener.onClick() position: " + position);
//
//                Meal mealToDelete = localData.get(position);
//                Long idMealToDelete = mealToDelete.getId();
//                requestDeleteMealById(idMealToDelete);
//            }
//        };
//        Meal meal = new Meal();
//        meal.setId(420L);
//        meal.setName("tender");
//        meal.setSize(4);
//        meal.setDrink("cola");
//        localData.add(meal);
//        mealAdapter = new MealAdapter(localData, itemClickListener);
        menuItemInfos = new ArrayList<>();
        adapter = new MenuItemInfoAdapter(menuItemInfos, new MenuItemInfoAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i(TAG, "MenuItemInfoAdapter onClick()");

                MenuItemInfo menuItemInfo = menuItemInfos.get(position);
                Toast.makeText(view.getContext(), menuItemInfo.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
        dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(mealAdapter);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);

//        requestGetAllMeals();
        // TODO: repeated task
        long periodInSeconds = 3L;
        requestRepeatingGetAllMeals = executor.scheduleWithFixedDelay(
                new Runnable() {
                    @Override
                    public void run() {
//                        requestGetAllMeals();
                        requestGetAllOrders();
                    }
                },
                0,
                periodInSeconds,
                TimeUnit.SECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        requestRepeatingGetAllMeals.cancel(true);
        requestQueue.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        // Get the MenuItem for the action item.
        actionMenuItem = menu.findItem(R.id.actionitem_add);

        View addMealActionView = actionMenuItem.getActionView();
        EditText etName = addMealActionView.findViewById(R.id.et_add_meal_name);
        etName.setTag("etName");
        EditText etSize = addMealActionView.findViewById(R.id.et_add_meal_size);
        etSize.setTag("etSize");
        EditText etDrink = addMealActionView.findViewById(R.id.et_add_meal_drink);
        etDrink.setTag("etDrink");
        Button buttonAddMeal = addMealActionView.findViewById(R.id.button_add_meal);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "buttonAddMeal.onClick(View)");

                String nameToPost = etName.getText().toString();
                int sizeToPost = 0;
                try {
                    sizeToPost = Integer.parseInt(etSize.getText().toString());
                } catch (NumberFormatException e) {
                    Log.e(TAG, "buttonAddMeal.onClick(View) PARSING value from etSize to an Integer: " + e);
                    Toast.makeText(MainActivity.this, "EditText (SIZE) unable to parse to Integer... returning", Toast.LENGTH_SHORT).show();

                    Log.e(TAG, "buttonAddMeal.onClick(View) actionMenuItem.collapseActionView() ERROR path");
                    actionMenuItem.collapseActionView();
                    return;
                }
                String drinkToPost = etDrink.getText().toString();

//                requestPostMeal(actionMenuItem, nameToPost, sizeToPost, drinkToPost);
            }
        });

        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.i(TAG, "onCreateOptionsMenu(Menu) expandListener.onMenuItemActionExpand(MenuItem)");
                // Do something when it expands.
                return true; // Return true to expand the action view.
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.i(TAG, "onCreateOptionsMenu(Menu) expandListener.onMenuItemActionCollapse(MenuItem)");
                // Do something when the action item collapses.

                Log.i(TAG, "onCreateOptionsMenu(Menu) expandListener.onMenuItemActionCollapse(MenuItem) HIDING keyboard");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addMealActionView.getWindowToken(), 0);
                return true; // Return true to collapse action view.
            }
        };
        // Assign the listener to that action item.
        actionMenuItem.setOnActionExpandListener(expandListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.actionitem_add:
                Log.i(TAG, "onOptionsItemSelected(MenuItem) R.id.actionitem_add");
                // TODO:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void requestDeleteMealById(Long id) {
//        Log.i(TAG, "requestDeleteMealById()");
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.DELETE,
//                URL_DELETE_MEAL_BY_ID + "/" + String.valueOf(id),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i(TAG, "requestDeleteMealById() onResponse(): " + response);
//
//                        requestGetAllMeals();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, "requestDeleteMealById() onErrorResponse(): " + error);
//                    }
//                });
//        requestQueue.add(stringRequest);
//    }

    private void requestGetAllOrders() {
        Log.i(TAG, "requestGetAllOrders()");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_GET_ALL_ORDERS,
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "START");
                        // Converts JSON string into MenuItemInfoListWrapper object
                        Log.e(TAG, response.toString());

//                        Gson gson = new GsonBuilder().create();
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                                .create();
                        DataStore dataStore = gson.fromJson(response.toString(), DataStore.class);
                        Log.e(TAG, dataStore.getData().get(0).getCreatedOn().toString());

                        // TODO: currently just getting the most recent order...
                        //   doing this to see something on screen...
                        //   must update adapter from MenuItemInfo to MenuItemInfoListWrapper
                        List<MenuItemInfoListWrapper> menuItemInfoListWrappersFromServer = dataStore.getData();
                        if (!menuItemInfoListWrappersFromServer.isEmpty()) {
                            MenuItemInfoListWrapper menuItemInfoListWrapperMostRecent = menuItemInfoListWrappersFromServer.get(
                                    menuItemInfoListWrappersFromServer.size() - 1
                            );

                            LocalDateTime createdOn = menuItemInfoListWrapperMostRecent.getCreatedOn();
                            Log.e(TAG, "createdOn: " + createdOn);

                            List<MenuItemInfo> menuItemInfosFromServer = menuItemInfoListWrapperMostRecent.getMenuItemInfos();

                            menuItemInfos.clear();
                            menuItemInfos.addAll(menuItemInfosFromServer);
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "menuItemInfoListWrappersFromServer.isEmpty()");
                        }
                        Log.e(TAG, "END");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e(TAG, "onErrorResponse(VolleyError)" + error);
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

//    private void requestGetAllMeals() {
//        Log.i(TAG, "requestGetAllMeals()");
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                URL_GET_ALL_MEALS,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.i(TAG, "jsonArrayRequest (onResponse)");
//
//                        if (response.length() > 0) {
//                            Log.i(TAG, "jsonArrayRequest (onResponse): response.length == " + response.length());
//
//                            Log.i(TAG, "jsonArrayRequest (onResponse) CLEARING localData");
//                            localData.clear();
//
//                            Gson gson = new Gson();
//                            try {
//                                for (int i = 0; i < response.length(); i++) {
//                                    JSONObject json = response.getJSONObject(i);
//                                    Meal mealFromResponse = gson.fromJson(json.toString(), Meal.class);
//                                    localData.add(mealFromResponse);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            Log.i(TAG, "jsonArrayRequest (onResponse): fetchedMeals added to localData. Calling mealAdapter.notifyDataSetChanged();");
//                            mealAdapter.notifyDataSetChanged();
//                        } else {
//                            Log.i(TAG, "jsonArrayRequest (onResponse): response.length <= 0");
//                            Toast.makeText(MainActivity.this, "JSONArray response is empty", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, "jsonArrayRequest (onErrorResponse): " + error);
//                    }
//                });
//        requestQueue.add(jsonArrayRequest);
//    }

//    private void requestPostMeal(MenuItem actionMenuItem, String name, int size, String drink) {
//        Log.i(TAG, "requestPostMeal()");
//        StringRequest stringRequest =
//                new StringRequest(
//                        Request.Method.POST,
//                        URL_ADD_NEW_MEAL,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                String formatString = "The id of the posted Meal object is: %s";
//                                Log.i(TAG, "requestPostMeal() onResponse(): " + String.format(formatString, response));
//
//                                View addMealActionView = actionMenuItem.getActionView();
//                                EditText etName = addMealActionView.findViewWithTag("etName");
//                                etName.setText("");
//                                EditText etSize = addMealActionView.findViewWithTag("etSize");
//                                etSize.setText("");
//                                EditText etDrink = addMealActionView.findViewWithTag("etDrink");
//                                etDrink.setText("");
//
//                                requestGetAllMeals();
//
//                                Log.i(TAG, "requestPostMeal() onResponse() actionMenuItem.collapseActionView() HAPPY path");
//                                actionMenuItem.collapseActionView();
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.e(TAG, "requestPostMeal() onErrorResponse(): " + error);
//
//                                Log.e(TAG, "requestPostMeal() onErrorResponse() actionMenuItem.collapseActionView() ERROR path");
//                                actionMenuItem.collapseActionView();
//                            }
//                        }
//                ) {
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("name", name);
//                        params.put("size", Integer.toString(size));
//                        params.put("drink", drink);
//                        return params;
//                    }
//                };
//
//        requestQueue.add(stringRequest);
//    }
}
