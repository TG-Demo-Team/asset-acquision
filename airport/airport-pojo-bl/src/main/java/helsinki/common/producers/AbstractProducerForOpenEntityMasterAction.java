package helsinki.common.producers;

import static ua.com.fielden.platform.entity.AbstractEntity.KEY;

import java.util.Optional;

import helsinki.common.exceptions.GenericApplicationException;
import ua.com.fielden.platform.entity.AbstractEntity;
import ua.com.fielden.platform.entity.AbstractFunctionalEntityToOpenCompoundMaster;
import ua.com.fielden.platform.entity.DefaultEntityProducerWithContext;
import ua.com.fielden.platform.entity.factory.EntityFactory;
import ua.com.fielden.platform.entity.factory.ICompanionObjectFinder;
import ua.com.fielden.platform.utils.EntityUtils;

/**
 * A base type that should be applicable in most cases for implementing open entity master action producers.
 * 
 * @author Generated
 *
 * @param <T> -- entity type
 * @param <A> -- action type
 */
public abstract class AbstractProducerForOpenEntityMasterAction<T extends AbstractEntity<?>, A extends AbstractFunctionalEntityToOpenCompoundMaster<T>> extends DefaultEntityProducerWithContext<A> {

    protected final Class<T> entityType;
    
    public AbstractProducerForOpenEntityMasterAction(
            final EntityFactory factory,
            final Class<T> entityType,
            final Class<A> openActionType,            
            final ICompanionObjectFinder companionFinder) {
        super(factory, openActionType, companionFinder);
        this.entityType = entityType;
    }
    
    @Override
    protected A provideDefaultValues(final A openAction) {
        if (currentEntityInstanceOf(entityType) && chosenPropertyRepresentsThisColumn()) {
            // Edit current T row by clicking "this" column on entity T centre
            openAction.setKey(refetch(currentEntity(entityType), KEY));
        } else if (currentEntityInstanceOf(entityType) && chosenPropertyEmpty()) {
            // Primary action on entity T centre (edit entity)
            openAction.setKey(refetch(currentEntity(entityType), KEY));
        } else if (currentEntityNotEmpty() && chosenPropertyNotEmpty()) {
            // There are two possible legitimate cases here:
            // 1. Either currentEntity().get(chosenProperty()) is of type T and all is good, or
            // 2. ChosenProperty is a sub property of a property of type T, where that "parent" property belongs to the current entity, or
            // 3. We have a genuine bug and need to throw an appropriate error.
            final Optional<T> optClickedEntity = EntityUtils.traversePropPath(currentEntity(), chosenProperty())
                    .filter(t2 -> t2._2.map(v -> entityType.isAssignableFrom(v.getType())).orElse(false)) // find only type-compatible values on path
                    .map(t2 -> entityType.cast(t2._2.get())).findFirst();
            openAction.setKey(refetch(optClickedEntity.orElseThrow(() -> new GenericApplicationException("There is no entity to open.")), KEY));
        } else if (masterEntityEmpty() && selectedEntitiesEmpty()) {
            // Add new entity T
            openAction.setKey(co(entityType).new_());
        } else {
            // It is recommended to throw 'unsupported case' exception otherwise
            throw new GenericApplicationException("Not supported yet.");
        }
        return openAction;
    }
}
