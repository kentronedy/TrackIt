package com.aloydev.weighttracker;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TrackItDataService {

    Context context;

    public TrackItDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(JSONObject response) throws JSONException;
    }

    public void loginUser(String username, String password, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/users/login";


        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void registerUser(String username, String password, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/users/register";


        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void setGoal(String username, double goal, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/users/add_goal";


        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("goal", goal);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void setPermission(String username, int permission, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/users/change_permission";


        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("permission", permission);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void getUserData(String username, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/users/data_return";


        JSONObject request = new JSONObject();
        request.put("username", username);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }



    public void makeEntryWithWeight(Date date, String username, double weight, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/entries/entry_weight";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = format.format(date);
        long dateInt = date.getTime();

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("date", stringDate);
        request.put("dateInt", dateInt);
        request.put("weight", weight);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void makeEntryWithSleep(Date date, String username, double sleep, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/entries/entry_sleep";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = format.format(date);
        long dateInt = date.getTime();

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("date", stringDate);
        request.put("dateInt", dateInt);
        request.put("sleep", sleep);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void updateWeight(Date date, String username, double weight, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/entries/update_weight";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = format.format(date);

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("date", stringDate);
        request.put("weight", weight);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void updateSleep(Date date, String username, double sleep, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/entries/update_sleep";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = format.format(date);

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("date", stringDate);
        request.put("sleep", sleep);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void countEntry(Date date, String username, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/entries/count";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = format.format(date);

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("date", stringDate);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void getEntry(Date date, String username, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/entries/get_entry";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = format.format(date);

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("date", stringDate);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public void deleteEntry(Date date, String username, final VolleyResponseListener vrl) throws JSONException {
        String url = "http://10.0.2.2:8080/weight_api/entries/delete_entry";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate = format.format(date);

        JSONObject request = new JSONObject();
        request.put("username", username);
        request.put("date", stringDate);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void getEntries(String username, final VolleyResponseListener vrl) throws JSONException {

        String url = "http://10.0.2.2:8080/weight_api/entries/return_entries";

        JSONObject request = new JSONObject();
        request.put("username", username);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            vrl.onResponse(response);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        vrl.onError(error.getMessage());
                    }
                });

        // Add a request to your RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
