package repository;

public interface IRepository<E> {
    /**
     * finds all the entities
     * @return a list of the entities
     */
    public Iterable<E> getAll();

    /**
     * finds a certain entity based on it's id
     * @param id Long
     * @return the entity
     */
    public E findOne(Long id);

    /**
     * saves an entity
     * @param entity E
     */
    public void save(E entity);

    /**
     * removes an entity based on it's id
     * @param id Long
     */
    public void delete(Long id);

    /**
     * updates an entity based on it's id
     * @param entity E
     */
    public void update(E entity);
}
