package org.craft.spoonge.service;

import java.util.*;

import com.google.common.collect.*;

import org.spongepowered.api.service.*;

public class SpoongeServiceManager implements ServiceManager
{

    private HashMap<Class<?>, Object> providers;

    public SpoongeServiceManager()
    {
        providers = Maps.newHashMap();
    }

    @Override
    public <T> void setProvider(Object plugin, Class<T> service, T provider) throws ProviderExistsException
    {
        if(providers.containsKey(service))
            throw new ProviderExistsException("Provider for service " + service.getSimpleName() + " already exists");
        providers.put(service, provider);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> com.google.common.base.Optional<T> provide(Class<T> service)
    {
        return com.google.common.base.Optional.of((T) providers.get(service));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T provideUnchecked(Class<T> service) throws ProvisioningException
    {
        if(!providers.containsKey(service))
            throw new ProvisioningException("Provider doesn't exist", service);
        return (T) providers.get(service);
    }

}
