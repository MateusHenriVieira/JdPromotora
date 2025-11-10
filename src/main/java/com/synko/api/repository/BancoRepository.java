package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.Banco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class BancoRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "bancos";

    public String save(Banco banco) throws ExecutionException, InterruptedException {
        if (banco.getId() == null) {
            banco.setId(firestore.collection(COLLECTION_NAME).document().getId());
            banco.setDataCadastro(Timestamp.now());
        }

        firestore.collection(COLLECTION_NAME).document(banco.getId()).set(banco).get();
        return banco.getId();
    }

    public Banco findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return document.toObject(Banco.class);
        }
        return null;
    }

    public List<Banco> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Banco> bancos = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            bancos.add(document.toObject(Banco.class));
        }
        return bancos;
    }

    public Banco findByNome(String nome) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).whereEqualTo("nome", nome).limit(1).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (!documents.isEmpty()) {
            return documents.get(0).toObject(Banco.class);
        }
        return null;
    }

    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public List<Banco> search(String nome, Boolean ativo) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME);

        if (nome != null && !nome.isEmpty()) {
            query = query.whereGreaterThanOrEqualTo("nome", nome)
                    .whereLessThanOrEqualTo("nome", nome + "\uf8ff");
        }
        if (ativo != null) {
            query = query.whereEqualTo("ativo", ativo);
        }

        ApiFuture<QuerySnapshot> future = query.get();
        List<Banco> bancos = new ArrayList<>();
        for (QueryDocumentSnapshot document : future.get().getDocuments()) {
            bancos.add(document.toObject(Banco.class));
        }
        return bancos;
    }
}