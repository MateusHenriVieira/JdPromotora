package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.Supervisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class SupervisorRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "supervisores";

    /**
     * Salva ou atualiza um supervisor.
     */
    public String save(Supervisor supervisor) throws ExecutionException, InterruptedException {
        if (supervisor.getId() == null) {
            supervisor.setId(firestore.collection(COLLECTION_NAME).document().getId());
            supervisor.setDataCadastro(Timestamp.now());
        }

        firestore.collection(COLLECTION_NAME).document(supervisor.getId()).set(supervisor).get();
        return supervisor.getId();
    }

    /**
     * Busca um supervisor específico pelo seu ID.
     */
    public Supervisor findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Supervisor.class);
        }
        return null;
    }

    /**
     * Lista todos os supervisores.
     */
    public List<Supervisor> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        return future.get().toObjects(Supervisor.class);
    }

    /**
     * Busca um supervisor pelo CPF, que deve ser único.
     */
    public Supervisor findByCpf(String cpf) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("cpf", cpf)
                .limit(1)
                .get();

        List<Supervisor> supervisores = future.get().toObjects(Supervisor.class);
        return supervisores.isEmpty() ? null : supervisores.get(0);
    }

    /**
     * Lista todos os supervisores de um gerente específico.
     */
    public List<Supervisor> findAllByGerenteId(String gerenteId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("gerenteId", gerenteId)
                .get();

        return future.get().toObjects(Supervisor.class);
    }

    /**
     * Deleta um supervisor pelo seu ID.
     */
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}