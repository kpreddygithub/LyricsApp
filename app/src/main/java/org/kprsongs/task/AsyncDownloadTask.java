package org.kprsongs.task;

/**
 * @Author : K Purushotham Reddy
 * @Version : 1.0
 */
//public class AsyncDownloadTask extends AsyncTask<String, Void, Boolean>
//{
//    public static final String GET_REQUEST = "GET";
//    public static final String DATABASE_UPDATED_DATE_KEY = "databaseUpdatedDateKey";
//    public static final String DATE_PATTERN = "dd/MM/yyyy";
//    private File externalCacheDir;
//    private File commonPropertyFile;
//    private SongDao songDao;
//
//    public AsyncDownloadTask()
//    {
//
//    }
//
//    public AsyncDownloadTask(Context context)
//    {
//        songDao = new SongDao(context);
//        commonPropertyFile = PropertyUtils.getPropertyFile(context, CommonConstants.COMMON_PROPERTY_TEMP_FILENAME);
//        externalCacheDir = context.getExternalCacheDir();
//    }
//
//    @Override
//    protected void onPreExecute()
//    {
//
//    }
//
//    @Override
//    protected void onProgressUpdate(Void... values)
//    {
//
//    }
//
//    @Override
//    protected Boolean doInBackground(String... strings)
//    {
//        File destinationFile = null;
//        try {
//            // String remoteUrl = "https://raw.githubusercontent.com/crunchersaspire/worshipsongs-db/master/songs.sqlite";
//            String remoteUrl = "https://raw.githubusercontent.com/crunchersaspire/worshipsongs-db-dev/master/songs.sqlite";
//            String className = this.getClass().getSimpleName();
//            destinationFile = File.createTempFile("download-songs", "sqlite", externalCacheDir);
//            URL url = new URL(remoteUrl);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod(GET_REQUEST);
//            urlConnection.setConnectTimeout(60000);
//            urlConnection.setReadTimeout(60000);
//            urlConnection.setDoOutput(true);
//            urlConnection.connect();
//            DataInputStream dataInputStream = new DataInputStream(urlConnection.getInputStream());
//            int contentLength = urlConnection.getContentLength();
//            Log.d(className, "Content length " + contentLength);
//            byte[] buffer = new byte[contentLength];
//            dataInputStream.readFully(buffer);
//            dataInputStream.close();
//            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(destinationFile));
//            dataOutputStream.write(buffer);
//            dataOutputStream.flush();
//            dataOutputStream.close();
//            Log.i(className, "Finished downloading file!");
//            songDao.copyDatabase(destinationFile.getAbsolutePath(), true);
//            songDao.open();
//            PropertyUtils.setProperty(DATABASE_UPDATED_DATE_KEY, DateFormatUtils.format(new Date(), DATE_PATTERN), commonPropertyFile);
//            return true;
//        } catch (Exception ex) {
//            Log.e(this.getClass().getSimpleName(), "Error", ex);
//            return false;
//        } finally {
//            destinationFile.deleteOnExit();
//        }
//    }
//
//    @Override
//    protected void onPostExecute(Boolean aBoolean)
//    {
//
//    }
//}
