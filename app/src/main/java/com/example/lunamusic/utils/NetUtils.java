package com.example.lunamusic.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.lunamusic.entity.Song;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetUtils {
    public static boolean bIsNetMod = false;
    public static Map<Integer,String> netArtMap = new HashMap<>();
    final static String API_URL = "https://api.imjad.cn/cloudmusic";
    final static int FAV_LIST_ID = 10691315;

    private static URL getUrl(String type, int id) throws MalformedURLException {
        return new URL(API_URL + "/?type=" + type + "&id=" + id);
    }

    public static ArrayList<Song> getAllNetMusic() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        ArrayList<Song> songList = new ArrayList<>();

        JSONObject jsonObject = JSON.parseObject(getJSONWithHttpsURLConnection(getUrl("playlist", FAV_LIST_ID)));

        if (jsonObject.isEmpty()) {
            System.out.println("MYFLAG_NetworkError");
            return songList;
        }

        JSONArray jsonArray = jsonObject
                .getJSONObject("playlist")
                .getJSONArray("tracks");

        for (int i = 0; i < 7; i++) {
            JSONObject al = ((JSONObject) jsonArray.get(i)).getJSONObject("al");
            int alId = al.getInteger("id");

            netArtMap.put(alId, al.getString("picUrl"));

            JSONObject ob_album = JSON.parseObject(getJSONWithHttpsURLConnection(getUrl("album", alId)));
            JSONArray songs = ob_album.getJSONArray("songs");

            for (int j = 0; j < 2; j++) {
                JSONObject song = (JSONObject) songs.get(j);
                songList.add(J2S(song));
            }
        }
        return songList;

    }

    public static String getNetArtUri(Integer album_id) {
        String temp = netArtMap.get(album_id);
        return temp;
    }

    private static String getJSONWithHttpsURLConnection(URL url) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, KeyManagementException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        TrustManager[] tm = {new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tm, new java.security.SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        connection.setSSLSocketFactory(ssf);

        StringBuilder response = new StringBuilder();
        try {
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\r\n");
            }
            reader.close();
            return response.toString();
        } catch (Exception e) {
            System.out.println("MYLOG_ERROR_START");
            e.printStackTrace();
            System.out.println("MYLOG_ERROR_END");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }

    private static Song J2S(JSONObject typeSong) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {

        JSONObject ar = (JSONObject) (typeSong.getJSONArray("ar").get(0));
        JSONObject al = typeSong.getJSONObject("al");
        int songId = typeSong.getInteger("id");

        JSONObject fuck = JSON.parseObject(getJSONWithHttpsURLConnection(getUrl("song", songId)));
        JSONObject data = (JSONObject) fuck.getJSONArray("data").get(0);
        String url = data.getString("url");

        Song song = new Song();
        song.setDuration(1);
        song.setTitle(typeSong.getString("name"));
        song.setId(songId);
        song.setArtist(ar.getString("name"));
        song.setPath(url);
        song.setSize(1);
        song.setAlbumId(al.getInteger("id"));
        song.setAlbumTitle(al.getString("name"));

        return song;
    }

}
