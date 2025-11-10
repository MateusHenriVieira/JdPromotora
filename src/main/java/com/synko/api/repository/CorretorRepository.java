package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.Corretor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class CorretorRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "corretores";

    public String save(Corretor corretor) throws ExecutionException, InterruptedException {
        if (corretor.getId() == null) {
            corretor.setId(firestore.collection(COLLECTION_NAME).document().getId());
            corretor.setDataCadastro(Timestamp.now());
        }
        corretor.setDataAtualizacao(Timestamp.now());

        firestore.collection(COLLECTION_NAME).document(corretor.getId()).set(corretor).get();
        return corretor.getId();
    }

    public Corretor findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Corretor.class);
        }
        return null;
    }

    public List<Corretor> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<Corretor> corretores = new ArrayList<>();
        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            corretores.add(document.toObject(Corretor.class));
        }
        return corretores;
    }

    /**
     * Busca um corretor pelo CPF/CNPJ, que deve ser único.
     */
    public Corretor findByCpfCnpj(String cpfCnpj) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("cpfCnpj", cpfCnpj)
                .limit(1)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()) {
            return documents.get(0).toObject(Corretor.class);
        }
        return null;
    }

    /**
     * Lista todos os corretores de uma empresa específica.
     */
    public List<Corretor> findAllByEmpresaId(String empresaId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("empresaId", empresaId)
                .get();

        List<Corretor> corretores = new ArrayList<>();
        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            corretores.add(document.toObject(Corretor.class));
        }
        return corretores;
    }

    /**
     * Lista todos os corretores de uma Rota específica.
     * Necessário para a lógica de deleção em cascata do RotaService.
     */
    public List<Corretor> findAllByRotaId(String rotaId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("rotaId", rotaId)
                .get();

        return future.get().toObjects(Corretor.class);
    }

    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }
}