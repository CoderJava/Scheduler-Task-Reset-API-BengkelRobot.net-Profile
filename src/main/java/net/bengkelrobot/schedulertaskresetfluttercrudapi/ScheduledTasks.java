package net.bengkelrobot.schedulertaskresetfluttercrudapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.bengkelrobot.schedulertaskresetfluttercrudapi.model.Profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
            "HH:mm:ss");

    // Execute at fixed interval by using fixedRate
    /*@Scheduled(fixedRate = 2000)
    public void scheduleTaskWithFixedRate() {
        logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(
                LocalDateTime.now()));
    }*/

    // Execute a task with fixed delay between the completion of the last invocation and the
    // start of
    // the next, using fixedDelay parameter
    /*@Scheduled(fixedDelay = 2000)
    public void scheduleTaskWithFixedDelay() {
        logger.info("Fixed Delay Task :: Execution Time - {}", dateTimeFormatter.format
        (LocalDateTime.now()));
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.error("Ran into error {}", e);
            throw new IllegalStateException(e);
        }
    }*/

    /*public void scheduleTaskWithInitialDelay() {
    }*/

    // cron = "[seconds] [minutes] [hours] [day of month] [month] [day of week] [year]"
    /*@Scheduled(fixedRate = 1000 * 60, zone = "Asia/Jakarta")*/
    @Scheduled(cron = "0 0 10 * * *")
    public void scheduleTaskWithCronExpression() {
        logger.info("Cron Task :: Execution Time - {}",
                dateTimeFormatter.format(LocalDateTime.now()));
        ResponseBody body = null;
        try {
            final String baseUrl = "http://api.bengkelrobot.net:8001/api";
            Request request = new Request.Builder()
                    .url(baseUrl + "/profile")
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();
            Call call = client.newCall(request);
            Response responseAllProfile = call.execute();
            if (responseAllProfile.code() == 200) {
                body = responseAllProfile.body();
                String strResponseBody = body.string();
                Gson gson = new GsonBuilder().create();
                List<Profile> listProfiles = gson.fromJson(strResponseBody,
                        new TypeToken<List<Profile>>() {
                        }.getType());
                listProfiles.forEach(profile -> {
                    Request requestDeleteProfile = new Request.Builder()
                            .url(baseUrl + "/profile/" + profile.getId())
                            .delete()
                            .build();
                    Call callDeleteProfile = client.newCall(requestDeleteProfile);
                    Response responseDeleteProfile;
                    try {
                        responseDeleteProfile = callDeleteProfile.execute();
                        if (responseDeleteProfile.code() == 200) {
                            logger.info("Cron Task :: Delete Profile with id " + profile.getId() + " success");
                        } else {
                            logger.error("Cron Task :: Error - Delete profile != 200 {}", responseDeleteProfile.message());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("Cron Task :: Error - Delete profile error - ", e);
                    }
                });

                Profile defaultProfile = new Profile(1, "User 1", "user1@gmail.com", 21);
                String jsonDefaultProfile = gson.toJson(defaultProfile);
                Request requestPostDefaultProfile = new Request.Builder()
                        .url(baseUrl + "/profile")
                        .post(RequestBody.create(MediaType.parse("application/json"), jsonDefaultProfile))
                        .build();
                Call callPostProfile = client.newCall(requestPostDefaultProfile);
                Response responsePostProfile = callPostProfile.execute();
                if (responsePostProfile.code() == 201) {
                    logger.info("Cron Task :: Post default Profile success");
                } else {
                    logger.info("Cron Task :: Post default Profile failed {}", responsePostProfile.message());
                }
            } else {
                logger.error("Cron Task :: Error - Get all profile != 200 {}", responseAllProfile.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Cron Task :: Error - ", e);
        } finally {
            if (body != null) {
                body.close();
            }
        }
    }
}
