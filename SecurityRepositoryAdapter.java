package org.investment;

import java.util.List;

public class SecurityRepositoryAdapter implements ISecurityRepository {
    private final AbstractSecurityRepository fileRepository;

    public SecurityRepositoryAdapter(AbstractSecurityRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public Security getById(int id) {
        return fileRepository.getById(id);
    }

    @Override
    public List<BriefSecurity> get_k_n_short_list(int k, int n, String sortField) {
        return fileRepository.get_k_n_short_list(k, n, sortField);
    }

    @Override
    public void addSecurity(Security security) {
        fileRepository.addSecurity(security);
    }

    @Override
    public void replaceSecurity(int id, Security newSecurity) {
        fileRepository.replaceSecurity(id, newSecurity);
    }

    @Override
    public void deleteSecurity(int id) {
        fileRepository.deleteSecurity(id);
    }

    @Override
    public int get_count() {
        return fileRepository.get_count();
    }

    @Override
    public void sort_by_field(String field) {
        fileRepository.sort_by_field(field);
    }
}
