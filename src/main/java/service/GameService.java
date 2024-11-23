package service;

import com.google.cloud.firestore.DocumentSnapshot;
import models.Game;

import java.util.concurrent.ExecutionException;

public class GameService extends BaseService<Game> {

    @Override
    public Game getById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = firestore.collection("games").document(id).get().get();
        return snapshot.toObject(Game.class);
    }

    @Override
    public void save(Game game) throws ExecutionException, InterruptedException {
        firestore.collection("games").document(game.getGameId()).set(game).get();
    }
}