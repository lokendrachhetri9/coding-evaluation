package com.aa.act.interview.org;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Organization {

    private AtomicInteger employeeId;

    private Position root;

    public Organization() {
        root = createOrganization();
        this.employeeId = new AtomicInteger(1);
    }
    
    protected abstract Position createOrganization();
    
    /**
     * hire the given person as an employee in the position that has that title
     * 
     * @param person
     * @param title
     * @return the newly filled position or empty if no position has that title
     */
    public Optional<Position> hire(Name person, String title) {
        Optional<Position> positionOpt = getPositionByTitle(root, title);
        if(positionOpt.isEmpty()){
            return  Optional.empty();
        }

        Position position = positionOpt.get();

        position.setEmployee(
                Optional.of(
                        new Employee(employeeId.getAndIncrement(),
                person)));

        return Optional.of(position);
    }

    @Override
    public String toString() {
        return printOrganization(root, "");
    }
    
    private String printOrganization(Position pos, String prefix) {
        StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
        for(Position p : pos.getDirectReports()) {
            sb.append(printOrganization(p, prefix + "  "));
        }
        return sb.toString();
    }

    private Optional<Position> getPositionByTitle(Position pos, String title) {

        if(pos.getTitle().equals(title)){
            return Optional.of(pos);
        }

        for(Position p : pos.getDirectReports()){
            Optional<Position> positionOpt = getPositionByTitle(p, title);
            if(positionOpt.isPresent()){
                return positionOpt;
            }
        }

        return Optional.empty();
    }
}