package org.investment;

import org.investment.util.BaseObservable;
import java.util.List;

public class SecurityRepositoryAdapter extends BaseObservable implements ISecurityRepository {
    private final AbstractSecurityRepository fileRepository;

    public SecurityRepositoryAdapter(AbstractSecurityRepository fileRepository) {
        this.fileRepository = fileRepository;
        this.fileRepository.readFromFile();
    }

    @Override
    public Security getById(int id) {
        fileRepository.readFromFile();
        return fileRepository.getById(id);
    }

    @Override
    public List<BriefSecurity> get_k_n_short_list(int k, int n, String sortField) {
        fileRepository.readFromFile();
        return fileRepository.get_k_n_short_list(k, n, sortField);
    }

    @Override
    public void addSecurity(Security security) {
        try {
            fileRepository.readFromFile();
            fileRepository.addSecurity(security);
            fileRepository.writeToFile();
            notifyObservers();
        } catch (DuplicateSecurityNameException e) {
            throw e;
        }
    }

    @Override
    public void replaceSecurity(int id, Security newSecurity) {
        try {
            fileRepository.readFromFile();
            fileRepository.replaceSecurity(id, newSecurity);
            fileRepository.writeToFile();
            notifyObservers();
        } catch (DuplicateSecurityNameException e) {
            throw e;
        }
    }

    @Override
    public void deleteSecurity(int id) {
        fileRepository.readFromFile();
        fileRepository.deleteSecurity(id);
        fileRepository.writeToFile();
        notifyObservers();
    }

    @Override
    public int get_count() {
        fileRepository.readFromFile();
        return fileRepository.get_count();
    }

    @Override
    public void sort_by_field(String field) {
        fileRepository.readFromFile();
        fileRepository.sort_by_field(field);
        fileRepository.writeToFile();
        notifyObservers();
    }
}