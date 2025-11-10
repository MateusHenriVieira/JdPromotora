package com.synko.api.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.synko.api.model.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class EmpresaRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "empresas";

    public String save(Empresa empresa) throws ExecutionException, InterruptedException {
        if (empresa.getId() == null) {
            empresa.setId(firestore.collection(COLLECTION_NAME).document().getId());
            empresa.setDataCadastro(Timestamp.now());
        }
        empresa.setDataAtualizacao(Timestamp.now());

        ApiFuture<WriteResult> future = firestore
                .collection(COLLECTION_NAME)
                .document(empresa.getId())
                .set(empresa);

        future.get();
        return empresa.getId();
    }

    public Empresa findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore
                .collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get();

        if (document.exists()) {
            return document.toObject(Empresa.class);
        }
        return null;
    }

    public List<Empresa> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore
                .collection(COLLECTION_NAME)
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Empresa> empresas = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            empresas.add(document.toObject(Empresa.class));
        }

        return empresas;
    }

    public Empresa findByCnpj(String cnpj) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore
                .collection(COLLECTION_NAME)
                .whereEqualTo("cnpj", cnpj)
                .limit(1) // Garante unicidade
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        if (!documents.isEmpty()) {
            return documents.get(0).toObject(Empresa.class);
        }
        return null;
    }

    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public List<Empresa> search(String nome, String cnpj, String token)
            throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME);

        if (nome != null && !nome.isEmpty()) {
            // Busca "começa com" para o nome
            query = query.whereGreaterThanOrEqualTo("nome", nome)
                    .whereLessThanOrEqualTo("nome", nome + "\uf8ff");
        }

        if (cnpj != null && !cnpj.isEmpty()) {
            query = query.whereEqualTo("cnpj", cnpj);
        }

        if (token != null && !token.isEmpty()) {
            query = query.whereEqualTo("tokenAutenticacao", token);
        }

        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Empresa> empresas = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            empresas.add(document.toObject(Empresa.class));
        }

        return empresas;
    }
}