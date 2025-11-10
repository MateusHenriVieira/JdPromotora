package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.Gerente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class GerenteRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "gerentes";

    /**
     * Salva ou atualiza um gerente.
     * Se o ID for nulo, cria um novo documento e seta a data de cadastro.
     * (O model Gerente no README não possui 'dataAtualizacao', então não é setado).
     */
    public String save(Gerente gerente) throws ExecutionException, InterruptedException {
        if (gerente.getId() == null) {
            gerente.setId(firestore.collection(COLLECTION_NAME).document().getId());
            gerente.setDataCadastro(Timestamp.now());
        }

        firestore.collection(COLLECTION_NAME).document(gerente.getId()).set(gerente).get();
        return gerente.getId();
    }

    /**
     * Busca um gerente específico pelo seu ID.
     */
    public Gerente findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Gerente.class);
        }
        return null;
    }

    /**
     * Lista todos os gerentes.
     */
    public List<Gerente> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        return future.get().toObjects(Gerente.class);
    }

    /**
     * Busca um gerente pelo CPF, que deve ser único.
     */
    public Gerente findByCpf(String cpf) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("cpf", cpf)
                .limit(1)
                .get();

        List<Gerente> gerentes = future.get().toObjects(Gerente.class);
        return gerentes.isEmpty() ? null : gerentes.get(0);
    }

    /**
     * Lista todos os gerentes de uma empresa específica.
     */
    public List<Gerente> findAllByEmpresaId(String empresaId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("empresaId", empresaId)
                .get();

        return future.get().toObjects(Gerente.class);
    }

    /**
     * Deleta um gerente pelo seu ID.
     */
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}