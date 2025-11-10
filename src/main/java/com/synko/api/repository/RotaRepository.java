package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.Rota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class RotaRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "rotas";

    /**
     * Salva ou atualiza uma rota.
     */
    public String save(Rota rota) throws ExecutionException, InterruptedException {
        if (rota.getId() == null) {
            rota.setId(firestore.collection(COLLECTION_NAME).document().getId());
            rota.setDataCadastro(Timestamp.now());
        }

        firestore.collection(COLLECTION_NAME).document(rota.getId()).set(rota).get();
        return rota.getId();
    }

    /**
     * Busca uma rota específica pelo seu ID.
     */
    public Rota findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Rota.class);
        }
        return null;
    }

    /**
     * Lista todas as rotas.
     */
    public List<Rota> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        return future.get().toObjects(Rota.class);
    }

    /**
     * Lista todas as rotas de um supervisor específico.
     */
    public List<Rota> findAllBySupervisorId(String supervisorId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("supervisorId", supervisorId)
                .get();

        return future.get().toObjects(Rota.class);
    }

    /**
     * Deleta uma rota pelo seu ID.
     */
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}