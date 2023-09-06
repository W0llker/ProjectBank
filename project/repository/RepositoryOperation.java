package project.repository;

import project.entity.Operation;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RepositoryOperation {
    private final static File file = new File("resources/operation");
    private static List<Operation> operations;

    public RepositoryOperation() {
        operations = new ArrayList<>();
    }

    public void serialization(Operation operation) {
        operations = deserialization();
        Optional<Operation> optionalId;
        if (operations.size() > 0) {
            optionalId = operations.stream().max(Comparator.comparingLong(Operation::getId));
            operation.setId(optionalId.get().getId() + 1);
        } else operation.setId(0);
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            System.out.println(operation);
            operations.add(operation);
            stream.writeObject(operations);
        } catch (Exception e) {

        }
    }

    public List<Operation> deserialization() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file))) {
            operations = (List<Operation>) stream.readObject();
        } catch (Exception e) {

        }
        return operations;
    }

    public void uploadOperation() {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            stream.writeObject(operations);
        } catch (Exception e) {
        }
    }
}
