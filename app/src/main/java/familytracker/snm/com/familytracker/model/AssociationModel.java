package familytracker.snm.com.familytracker.model;

import io.realm.RealmObject;

/**
 * Created by kumanjit on 10/4/2017.
 */

public class AssociationModel extends RealmObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
