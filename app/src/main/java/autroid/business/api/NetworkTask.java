package autroid.business.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

import autroid.business.R;

public class NetworkTask extends AsyncTask<String, String, String> {
    private boolean isDoinBackground, isPostExecute, isPreExecute;
    public boolean isProgressDialog = true;
    private String dialogMessage = "Loading";
    private DoinBackgroung doinBackgroung;
    private Result result;
    private PreNetwork preNetwork;
    private ProgressDialog pd;
    private Context ctx;
    private int id;
    private int arg1;
    private String arg2;
    private String jsonParams;

    public NetworkTask(Context ctx, int id, String jsonParam) {
        this.ctx = ctx;
        this.id = id;
        this.jsonParams = jsonParam;
    }

    public NetworkTask(Context ctx, int id) {
        this.ctx = ctx;
        this.id = id;

    }

    public NetworkTask(Context ctx, int id,
                       String jsonParam, int arg1, String arg2) {
        this.ctx = ctx;
        this.id = id;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.jsonParams = jsonParam;
    }

    public void setDialogMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

    public void exposeDoinBackground(DoinBackgroung doinBackgroung) {
        this.isDoinBackground = true;
        this.doinBackgroung = doinBackgroung;
    }

    public void exposePostExecute(Result result) {
        this.isPostExecute = true;
        this.result = result;
    }

    public void exposePreExecute(PreNetwork preNetwork) {
        this.isPreExecute = true;
        this.preNetwork = preNetwork;
    }

    public void setProgressDialog(boolean isProgress) {
        isProgressDialog = isProgress;
    }


    public interface DoinBackgroung {

        String doInBackground(Integer id, String... params);
    }

    public interface Result {
        void resultfromNetwork(String object, int id, int arg1, String arg2);
    }

    public interface PreNetwork {

        void preNetwork(int id);
    }

    @Override
    protected void onPreExecute() {

        if (ctx != null) {
            if (isProgressDialog) {
          /*  pd = ProgressDialog.show(ctx, null, dialogMessage);
            pd = new ProgressDialog(ctx);
            pd.show();
            pd.setContentView(R.layout.custom_progress_dialog);*/
                pd = new ProgressDialog(ctx, R.style.TransparentProgressDialog);
                pd.setMessage(dialogMessage);
                pd.setTitle(null);
//                WindowManager.LayoutParams params = pd.getWindow().getAttributes();
//                params.y = 100;
//                params.x=200;
//                pd.getWindow().setAttributes(params);
                pd.show();

                /*pd.setProgressStyle(R.style.TransparentProgressDialog);
                LayoutInflater inflater = LayoutInflater.from(ctx);
                View v = inflater.inflate(R.layout.custom_progress_dialog, null);
                ((TextView) v.findViewById(R.id.lodingText)).setText(dialogMessage);
                pd.setContentView(v);*/


                // Will close by simply pressing back ppdrÃ¡
                pd.setCancelable(false);
                pd.setIndeterminate(true);
            }
            if (isPreExecute && preNetwork != null)
                preNetwork.preNetwork(id);
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String responseString = null;
        if (isDoinBackground && doinBackgroung != null) {
            return doinBackgroung.doInBackground(id, params);

        } else {

            String url = params[0];
            Log.d("URL-->",url+","+jsonParams);
            responseString = sendPost(url,jsonParams);

            System.out.println("response: " + responseString);
            //if (responseString != null && !responseString.equals("")) { return new Gson().fromJson(responseString, BeanResponse.class); }
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String myresult) {
        try {
            if (ctx != null) {
                if (pd != null && isProgressDialog && pd.isShowing()) pd.dismiss();
                // if (v != null) ((CharTextView) v.findViewById(R.id.loading_text)).setAnimate(false);

                if (isPostExecute && result != null)
                    result.resultfromNetwork(myresult, id, arg1, arg2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPostExecute(myresult);
    }

    // public static final String USER_AGENT = "Mozilla/5.0";



    public static String sendPost(String _url,String jsonData)  {
        StringBuilder params=new StringBuilder("");
        String result="";
        try{
            String url =_url;
            URL obj = new URL(_url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");

            //add request header
            //con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("source", "CarEager");
            con.setRequestProperty("deviceId", "careager");
            con.setRequestProperty("transactionName", "getToken");
            con.setRequestProperty("browser", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/json");

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
            outputStreamWriter.write(jsonData);
            outputStreamWriter.flush();

            int responseCode = con.getResponseCode();
            con.getContentType();
            con.getHeaderFields();

            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + jsonData);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Content type : " +con.getContentType());
            System.out.println("Header Fields : " +con.getHeaderFields());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            result = response.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return  result;
        }

    }


    private final static String USER_AGENT = "Mozilla/5.0";

    public static String serverRequest(String URL) {
        // TODO Auto-generated method stub

        String text="";
        try {
            //String url = "http://ec2-52-74-60-197.ap-southeast-1.compute.amazonaws.com/webservices/all_route.php?";

            URI uri = new URI("",URL, null);
            String ll=uri.toASCIIString();
            URL obj = new URL(ll);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            Log.d("URL-->",ll);

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            Log.d("Response Code-->",responseCode+"");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            text=response.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.d("Response JSON-->",text);
        return text;
    }
}
