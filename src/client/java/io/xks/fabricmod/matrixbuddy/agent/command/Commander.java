package io.xks.fabricmod.matrixbuddy.agent.command;

import io.xks.fabricmod.matrixbuddy.agent.Agent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


public class Commander {
    private final HttpClient client = HttpClient.newHttpClient();

    public Commander(){

    }

    public void reportAgentStatus(Agent agent){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://example.com"))
                .build();

        CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        future.thenAccept(response -> {
            // This will run in a separate thread once the response is received
            String body = response.body();
            System.out.println(body);

            // Communicate with the main thread
            // Depending on your application, you might need to use a different method to communicate with the main thread
            // For example, you could use a shared data structure, or a library like EventBus
        });
    }

//    public Task pollingCommand()
}
