package pe.edu.utp.autofast.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.utp.autofast.entity.Tecnico;
import pe.edu.utp.autofast.repository.TecnicoRepository;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    public List<Tecnico> findAll() {
        return tecnicoRepository.findByActivoTrueOrderByNombreAsc();
    }

    public Optional<Tecnico> findById(Long id) {
        return tecnicoRepository.findById(id);
    }

    public Tecnico save(Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }

    public Tecnico update(Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }

    public void delete(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        tecnico.setActivo(false);
        tecnicoRepository.save(tecnico);
    }

    public long count() {
        return tecnicoRepository.count();
    }
}