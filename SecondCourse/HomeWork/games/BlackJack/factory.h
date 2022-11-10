#ifndef FACTORY_H
#define FACTORY_H

#include <map>

template 
<
typename AbstractProduct, 
typename IdentifierType,
typename ProductCreator
>
class Factory
{
public:
    Factory() = default;
    Factory(const Factory &) = delete;
	Factory & operator=(const Factory &) = delete;
    
    bool Register(const IdentifierType& id, ProductCreator creator)
    {
        return associations_.insert(make_pair(id, creator)).second;
    }
    bool Unregister(const IdentifierType& id)
    {
        return associations_.erase(id) == 1;
    }
    AbstractProduct* CreateObject(const IdentifierType& id)
    {
        auto it = associations_.find(id);
        if (it != associations_.end())
        {
            return (it->second)();
        }
        else
        {
            throw std::runtime_error("Undefined object, failed!");
        }
    }
    static Factory * getInstance()
    {
        static Factory factory;
        return &factory;
    }
private:
    std::map <IdentifierType, ProductCreator> associations_;
};

#endif