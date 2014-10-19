package is.ru.happyhour.async;

import is.ru.happyhour.HappyListFragment;

import android.os.AsyncTask;

public class LoadHappiesHttp extends AsyncTask<Void, Void, Boolean> {

    private HappyListFragment frag;

    public LoadHappiesHttp(HappyListFragment frag) {
        this.frag = frag;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(!isCancelled()) {
            System.out.println("download success: " + success);
            frag.downloadFinished(success);
        } else {
            System.out.println("Asnyctask was cancelled");
        }
    }

    protected Boolean doInBackground(Void... args) {
        System.out.println("doInBackground downloading happies!");

        //Simulation of downloading happy hours
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}