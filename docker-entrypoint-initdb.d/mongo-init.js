print('Start #################################################################');

db = db.getSiblingDB('ebi-person-service');
db.createUser({ user: 'ebi-person-service', pwd: 'ZWJpLXBlcnNvbi1zZXJ2aWNl', roles: [{ role: 'readWrite', db: 'ebi-person-service' }]});
db.createCollection('persons');

print('END #################################################################');