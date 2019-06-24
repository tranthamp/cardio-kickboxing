-- :name create-exercise! :! :n
-- :doc creates a new exercise
INSERT INTO exercises
(name, callout, switch)
VALUES (:name, :callout, :switch)

-- :name get-exercise :? :1
-- :doc retrieves a exercise record given the id
SELECT * FROM exercises
WHERE id = :id

-- :name delete-exercise! :! :n
-- :doc deletes an exercise record given the id
DELETE FROM exercises
WHERE id = :id

-- :name get-exercises :? :*
-- :doc selects all available exercises
SELECT * FROM exercises
