package jdbc.manager;

import java.sql.SQLException;
import java.util.List;

public interface Manager<Entity, Id> {


    void create(Entity entity);

    Entity getById(Id id) throws SQLException;

    List<Entity> getAll();

    void update(Entity entity);

    void delete(Id id);


}
