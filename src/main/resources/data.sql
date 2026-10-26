-- =====================================================================
-- RecipeManagementSystem - Initial Data
-- Replaces recipemanagementsystem.json, tags.json, and StartUp.java.
-- Loaded by Spring Boot via data.sql after Hibernate creates the schema
-- (requires spring.jpa.defer-datasource-initialization=true).
-- All passwords are BCrypt hashes of: password123
-- =====================================================================

-- ======================  TAGS (31)  =================================
INSERT INTO TAG (ID, NAME, DESCRIPTION, TYPE) VALUES
  (1,  'Budget-Friendly',  'Budget-Friendly meals with cheap ingredients',                 'Descriptive'),
  (2,  'Quick & Easy',     'This meal is easy and quick to make, less than 20 minutes',     'Descriptive'),
  (3,  'Baking',           'Cooking food in an oven',                                       'Cooking Method'),
  (4,  'Roasting',         'Cooking food in an oven with dry heat',                         'Cooking Method'),
  (5,  'SautÃ©ing',         'Cooking food quickly in a pan with a small amount of fat',      'Cooking Method'),
  (6,  'Frying',           'Cooking food in hot fat',                                       'Cooking Method'),
  (7,  'Grilling',         'Cooking food over direct heat',                                'Cooking Method'),
  (8,  'Steaming',         'Cooking food by steaming',                                     'Cooking Method'),
  (9,  'Slow-cooking',     'Cooking food slowly over a long period',                       'Cooking Method'),
  (10, 'One-Pot',          'Recipes requiring only one pot',                               'Cooking Method'),
  (11, 'Italian',          'Italian Cuisine',                                              'Cuisine'),
  (12, 'Mexican',          'Mexican Cuisine',                                              'Cuisine'),
  (13, 'Indian',           'Indian Cuisine',                                               'Cuisine'),
  (14, 'Chinese',          'Chinese Cuisine',                                              'Cuisine'),
  (15, 'Thai',             'Thai Cuisine',                                                 'Cuisine'),
  (16, 'Japanese',         'Japanese Cuisine',                                             'Cuisine'),
  (17, 'American',         'American Cuisine',                                             'Cuisine'),
  (18, 'Mediterranean',    'Mediterranean Cuisine',                                        'Cuisine'),
  (19, 'Middle Eastern',   'Middle Eastern Cuisine',                                       'Cuisine'),
  (20, 'Korean',           'Korean Cuisine',                                               'Cuisine'),
  (21, 'Vegan',            'Food suitable for vegans',                                      'Dietary Restriction'),
  (22, 'Vegetarian',       'Food suitable for vegetarians',                                'Dietary Restriction'),
  (23, 'Gluten-free',      'Food free of gluten',                                          'Dietary Restriction'),
  (24, 'Dairy-free',       'Food free of dairy products',                                  'Dietary Restriction'),
  (25, 'Nut-free',         'Food free of nuts',                                            'Dietary Restriction'),
  (26, 'Soy-free',         'Food free of soy products',                                    'Dietary Restriction'),
  (27, 'Keto',             'Food suitable for a ketogenic diet',                           'Dietary Restriction'),
  (28, 'Paleo',            'Food suitable for a paleo diet',                              'Dietary Restriction'),
  (29, 'Low-carb',         'Food low in carbohydrates',                                    'Dietary Restriction'),
  (30, 'Pescatarian',      'Food that includes fish and seafood but not meat',             'Dietary Restriction'),
  (31, 'High-Protein',     'Food that has a high amount of protein',                       'Descriptive');

-- ======================  USERS (5)  ==================================
-- All passwords are BCrypt hashes of: password123
-- 1-2: CHEFs (own recipemanagementsystem). 3-5: regular USERS (rate recipemanagementsystem).
INSERT INTO USER_ACCOUNT (ID, EMAIL, PASSWORD, AUTHORITY, IS_ACCOUNT_NON_LOCKED, IS_ENABLED) VALUES
  (1, 'chef.omar@email.com',  '$2a$10$NQO6b8ROgW6XZvL3iGhykOyVmoZqm2wzTMuBpW3HOWdPg1cjdDTMW', 'ROLE_CHEF',  TRUE, TRUE),
  (2, 'chef.lina@email.com',  '$2a$10$NQO6b8ROgW6XZvL3iGhykOyVmoZqm2wzTMuBpW3HOWdPg1cjdDTMW', 'ROLE_CHEF',  TRUE, TRUE),
  (3, 'user.ali@email.com',   '$2a$10$NQO6b8ROgW6XZvL3iGhykOyVmoZqm2wzTMuBpW3HOWdPg1cjdDTMW', 'ROLE_USER',  TRUE, TRUE),
  (4, 'user.sara@email.com',  '$2a$10$NQO6b8ROgW6XZvL3iGhykOyVmoZqm2wzTMuBpW3HOWdPg1cjdDTMW', 'ROLE_USER',  TRUE, TRUE),
  (5, 'user.kenji@email.com', '$2a$10$NQO6b8ROgW6XZvL3iGhykOyVmoZqm2wzTMuBpW3HOWdPg1cjdDTMW', 'ROLE_USER',  TRUE, TRUE);

-- ======================  RECIPES (25)  ===============================
INSERT INTO RECIPE (ID, NAME, DESCRIPTION, CATEGORY, DATE, TOTAL_RATING, CREATED_BY_ID) VALUES
  (1,  'Cheesy Tomato Pasta',                    'A quick and easy weeknight meal',                                       'Lunch,Dinner',         '2024-01-15', 0.0, 1),
  (2,  'Scrambled Eggs with Toast',               'A classic and satisfying breakfast',                                    'Lunch,Breakfast',      '2024-01-16', 0.0, 1),
  (3,  'Black Bean Burgers',                      'A delicious and vegetarian alternative to traditional burgers',         'Lunch,Dinner',         '2024-01-17', 0.0, 1),
  (4,  'Spicy Chicken Stir-Fry',                  'A quick and flavorful dish with a kick',                                'Lunch,Dinner',         '2024-01-18', 0.0, 1),
  (5,  'Greek Yogurt Parfait',                    'A healthy and delicious layered breakfast',                             'Lunch,Breakfast',      '2024-01-19', 0.0, 1),
  (6,  'One-Pot Lemon Garlic Chicken',            'A simple and flavorful one-pot meal',                                   'Dinner',               '2024-01-20', 0.0, 1),
  (7,  'Tuna Salad Sandwich',                     'A classic and easy lunch option',                                       'Lunch,Dinner,Breakfast','2024-01-21', 0.0, 1),
  (8,  'Chicken Caesar Salad',                    'A hearty and flavorful salad',                                          'Lunch,Dinner,Breakfast','2024-01-22', 0.0, 1),
  (9,  'Veggie Wraps',                            'A customizable and light lunch option',                                 'Lunch,Breakfast',      '2024-01-23', 0.0, 1),
  (10, 'Leftover Soup and Salad',                 'A quick and easy way to use up leftovers',                              'Lunch,Dinner',         '2024-01-24', 0.0, 1),
  (11, 'The Ultimate Grilled Cheese',             'An indulgent grilled cheese sandwich with melted mozzarella and cheddar','Lunch,Breakfast',     '2024-01-25', 0.0, 1),
  (12, 'Chicken Pesto Wraps',                     'Quick and easy wraps with chicken, pesto, and mozzarella',              'Lunch,Dinner',         '2024-01-26', 0.0, 1),
  (13, 'Easy Chicken Salad',                      'Creamy chicken salad with sweet grapes and crunchy celery',             'Lunch,Dinner,Breakfast','2024-01-27', 0.0, 1),
  (14, 'Tuna Melt on Cornbread',                  'Open-faced tuna melt with cornbread and melted cheese',                 'Lunch,Dinner,Breakfast','2024-01-28', 0.0, 1),
  (15, 'Turkey Spinach Salad with Maple Dressing','A vibrant salad with fresh spinach, savory turkey, walnuts, cranberries','Lunch,Breakfast',     '2024-01-29', 0.0, 1),
  (16, 'Korean Beef Bulgogi Stir-Fry',            'A quick and flavorful stir-fry with thinly sliced marinated beef',      'Lunch,Dinner',         '2024-01-30', 0.0, 1),
  (17, 'Vegetable Pad Thai',                      'A quick Thai noodle dish with tofu and crisp vegetables',               'Lunch,Dinner',         '2024-02-01', 0.0, 2),
  (18, 'Chicken Tikka Masala',                    'A creamy, spiced Indian curry with tender chicken',                     'Dinner',               '2024-02-02', 0.0, 2),
  (19, 'Sushi Rolls (Maki)',                      'Homemade maki rolls with salmon or tuna, cucumber, and avocado',        'Lunch,Dinner',         '2024-02-03', 0.0, 2),
  (20, 'Falafel Pita',                            'Crispy fried chickpea patties in warm pita with tahini',                'Lunch,Dinner',         '2024-02-04', 0.0, 2),
  (21, 'Hummus and Veggie Bowl',                  'A Mediterranean bowl of hummus, quinoa, and fresh vegetables',         'Lunch,Dinner',         '2024-02-05', 0.0, 2),
  (22, 'Bibimbap',                                'Korean rice bowl with beef, vegetables, fried egg, and gochujang',      'Lunch,Dinner',         '2024-02-06', 0.0, 2),
  (23, 'Margherita Pizza',                        'Classic Italian pizza with tomato, fresh mozzarella, and basil',       'Lunch,Dinner',         '2024-02-07', 0.0, 2),
  (24, 'Keto Cauliflower Bowl',                   'Low-carb roasted cauliflower bowl with chicken, avocado, and cheese',   'Lunch,Dinner',         '2024-02-08', 0.0, 2),
  (25, 'Shakshuka',                               'Eggs poached in a spiced tomato and pepper sauce',                      'Breakfast,Dinner',     '2024-02-09', 0.0, 2);

-- ======================  RECIPE INGREDIENTS  ========================
-- Recipe 1: Cheesy Tomato Pasta
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (1, '1 pound dried pasta (penne, rotini, or your favorite)'),
  (1, '1 (28-ounce) can crushed tomatoes'),
  (1, '1/2 cup grated Parmesan cheese'),
  (1, '1/4 cup chopped fresh basil (optional)'),
  (1, '1 tablespoon olive oil'),
  (1, '1 clove garlic, minced'),
  (1, 'Salt and pepper to taste');

-- Recipe 2: Scrambled Eggs with Toast
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (2, '2 eggs'),
  (2, '1 tablespoon milk'),
  (2, '1 tablespoon butter'),
  (2, 'Salt and pepper to taste'),
  (2, '2 slices bread');

-- Recipe 3: Black Bean Burgers
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (3, '1 (15-ounce) can black beans, rinsed and drained'),
  (3, '1/2 cup cooked brown rice'),
  (3, '1/4 cup rolled oats'),
  (3, '1/4 cup chopped red onion'),
  (3, '1/4 cup chopped bell pepper (any color)'),
  (3, '1 clove garlic, minced'),
  (3, '1 tablespoon olive oil'),
  (3, '1 tablespoon chopped fresh cilantro'),
  (3, '1/2 teaspoon chili powder'),
  (3, '1/4 teaspoon cumin'),
  (3, 'Salt and pepper to taste'),
  (3, 'Hamburger buns');

-- Recipe 4: Spicy Chicken Stir-Fry
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (4, '1 pound boneless, skinless chicken breasts, thinly sliced'),
  (4, '1 tablespoon soy sauce'),
  (4, '1 tablespoon cornstarch'),
  (4, '1 tablespoon vegetable oil'),
  (4, '1 red bell pepper, sliced'),
  (4, '1 green bell pepper, sliced'),
  (4, '1 cup broccoli florets'),
  (4, '1/2 cup chopped onion'),
  (4, '1 clove garlic, minced'),
  (4, '1/4 cup chicken broth'),
  (4, '1 tablespoon sriracha (or to taste)'),
  (4, '1 tablespoon brown sugar'),
  (4, '1 teaspoon sesame oil'),
  (4, 'Cooked rice (optional)');

-- Recipe 5: Greek Yogurt Parfait
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (5, '1 cup plain Greek yogurt'),
  (5, '1/4 cup granola'),
  (5, '1/4 cup fresh berries (your choice)'),
  (5, '1 tablespoon honey (optional)');

-- Recipe 6: One-Pot Lemon Garlic Chicken
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (6, '1 pound boneless, skinless chicken breasts, cut into bite-sized pieces'),
  (6, '1 tablespoon olive oil'),
  (6, '1/2 teaspoon dried oregano'),
  (6, '1/4 teaspoon garlic powder'),
  (6, 'Salt and pepper to taste'),
  (6, '1 cup long-grain white rice'),
  (6, '1 (14.5-ounce) can diced tomatoes, undrained'),
  (6, '1 cup chicken broth'),
  (6, '1/2 cup chopped fresh parsley'),
  (6, '1 lemon, sliced');

-- Recipe 7: Tuna Salad Sandwich
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (7, '2 slices of bread'),
  (7, '1 can (5 oz) tuna in water, drained'),
  (7, '1/4 cup mayonnaise'),
  (7, '1 tablespoon chopped celery'),
  (7, '1/4 teaspoon dried onion powder'),
  (7, 'Salt and pepper to taste'),
  (7, 'Lettuce (optional)');

-- Recipe 8: Chicken Caesar Salad
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (8, '4 cups romaine lettuce, chopped'),
  (8, '1 grilled or baked chicken breast, sliced'),
  (8, '1/2 cup croutons'),
  (8, '1/4 cup grated Parmesan cheese'),
  (8, 'Caesar salad dressing (store-bought or homemade)');

-- Recipe 9: Veggie Wraps
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (9, '2 whole wheat tortillas'),
  (9, '1 cup chopped mixed greens'),
  (9, '1/2 cup hummus'),
  (9, '1/4 cup chopped cucumber'),
  (9, '1/4 cup chopped bell pepper (any color)'),
  (9, '1/4 cup shredded carrots'),
  (9, '1/4 cup crumbled feta cheese (optional)'),
  (9, 'Olive oil (optional)');

-- Recipe 10: Leftover Soup and Salad
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (10, '1 serving leftover soup (any kind)'),
  (10, '2 cups mixed greens'),
  (10, '1/4 cup cherry tomatoes, halved'),
  (10, '1/4 cup crumbled feta cheese (optional)'),
  (10, 'Vinaigrette dressing (optional)');

-- Recipe 11: The Ultimate Grilled Cheese
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (11, 'Cream cheese'),
  (11, 'Mozzarella'),
  (11, 'Cheddar'),
  (11, 'Italian bread');

-- Recipe 12: Chicken Pesto Wraps
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (12, 'Ground chicken'),
  (12, 'Sun-dried tomato pesto'),
  (12, 'Mozzarella');

-- Recipe 13: Easy Chicken Salad
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (13, 'Shredded rotisserie chicken'),
  (13, 'Seedless red grapes'),
  (13, 'Chopped pecans');

-- Recipe 14: Tuna Melt on Cornbread
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (14, 'Cornbread/muffin mix'),
  (14, 'Canned tuna'),
  (14, 'Celery'),
  (14, 'Avocado');

-- Recipe 15: Turkey Spinach Salad with Maple Dressing
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (15, 'Baby spinach'),
  (15, 'Deli smoked turkey'),
  (15, 'Maple syrup');

-- Recipe 16: Korean Beef Bulgogi Stir-Fry
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (16, '1 pound flank steak, thinly sliced'),
  (16, '1/4 cup soy sauce'),
  (16, '2 tablespoons brown sugar'),
  (16, '1 tablespoon honey'),
  (16, '1 tablespoon sesame oil'),
  (16, '1 tablespoon rice vinegar'),
  (16, '1 clove garlic, minced'),
  (16, '1 teaspoon grated ginger'),
  (16, '1/2 teaspoon red pepper flakes'),
  (16, '1 tablespoon vegetable oil'),
  (16, '1 bell pepper, sliced (any color)'),
  (16, '1 onion, sliced'),
  (16, '1 cup broccoli florets'),
  (16, '1/2 cup cooked rice'),
  (16, 'Sesame seeds (optional)'),
  (16, 'Chopped green onions (optional)');

-- Recipe 17: Vegetable Pad Thai
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (17, '8 oz rice noodles'),
  (17, '2 tablespoons vegetable oil'),
  (17, '2 cloves garlic, minced'),
  (17, '1 cup firm tofu, cubed'),
  (17, '1 cup mixed vegetables (carrots, bell peppers, bean sprouts)'),
  (17, '2 eggs, beaten (omit for vegan)'),
  (17, '3 tablespoons soy sauce'),
  (17, '2 tablespoons tamarind paste'),
  (17, '1 tablespoon brown sugar'),
  (17, '1 tablespoon lime juice'),
  (17, '1/4 cup crushed peanuts'),
  (17, '2 green onions, sliced'),
  (17, '1 lime wedge');

-- Recipe 18: Chicken Tikka Masala
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (18, '1.5 lbs boneless chicken thighs, cubed'),
  (18, '1 cup plain yogurt'),
  (18, '2 tablespoons lemon juice'),
  (18, '2 tablespoons garam masala'),
  (18, '1 tablespoon ginger, minced'),
  (18, '4 cloves garlic, minced'),
  (18, '2 tablespoons vegetable oil'),
  (18, '1 large onion, finely chopped'),
  (18, '1 (15-ounce) can tomato sauce'),
  (18, '1 cup heavy cream'),
  (18, '1 teaspoon paprika'),
  (18, 'Salt to taste'),
  (18, 'Fresh cilantro for garnish');

-- Recipe 19: Sushi Rolls (Maki)
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (19, '2 cups sushi rice'),
  (19, '2 1/2 cups water'),
  (19, '1/4 cup rice vinegar'),
  (19, '2 tablespoons sugar'),
  (19, '1 teaspoon salt'),
  (19, '4 nori sheets'),
  (19, '1 cucumber, julienned'),
  (19, '1 avocado, sliced'),
  (19, '4 oz sashimi-grade salmon or tuna'),
  (19, 'Soy sauce for serving'),
  (19, 'Pickled ginger for serving'),
  (19, 'Wasabi for serving');

-- Recipe 20: Falafel Pita
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (20, '2 (15-ounce) cans chickpeas, drained and dried'),
  (20, '1 small onion, chopped'),
  (20, '2 cloves garlic'),
  (20, '1/4 cup fresh parsley'),
  (20, '2 tablespoons flour'),
  (20, '1 teaspoon cumin'),
  (20, '1 teaspoon coriander'),
  (20, '1/2 teaspoon baking powder'),
  (20, 'Salt and pepper to taste'),
  (20, 'Vegetable oil for frying'),
  (20, '4 pita breads'),
  (20, '1 cup tahini sauce'),
  (20, '1 tomato, sliced'),
  (20, 'Lettuce');

-- Recipe 21: Hummus and Veggie Bowl
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (21, '1 cup hummus'),
  (21, '2 cups cooked quinoa or brown rice'),
  (21, '1 cucumber, diced'),
  (21, '1 cup cherry tomatoes, halved'),
  (21, '1/2 cup kalamata olives'),
  (21, '1/2 cup artichoke hearts, chopped'),
  (21, '1/4 cup red onion, diced'),
  (21, '2 tablespoons olive oil'),
  (21, '1 tablespoon lemon juice'),
  (21, '1 teaspoon dried oregano'),
  (21, 'Salt and pepper to taste'),
  (21, 'Fresh parsley for garnish');

-- Recipe 22: Bibimbap
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (22, '2 cups cooked short-grain rice'),
  (22, '1 lb beef sirloin, thinly sliced'),
  (22, '2 tablespoons soy sauce'),
  (22, '1 tablespoon sesame oil'),
  (22, '1 tablespoon sugar'),
  (22, '2 cloves garlic, minced'),
  (22, '1 zucchini, julienned'),
  (22, '1 carrot, julienned'),
  (22, '2 cups spinach'),
  (22, '4 eggs'),
  (22, '2 tablespoons gochujang'),
  (22, '1 tablespoon toasted sesame seeds'),
  (22, 'Vegetable oil for cooking');

-- Recipe 23: Margherita Pizza
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (23, '1 pizza dough ball (store-bought or homemade)'),
  (23, '1/2 cup tomato sauce'),
  (23, '8 oz fresh mozzarella, sliced (or vegan mozzarella)'),
  (23, '1/4 cup grated Parmesan (optional)'),
  (23, '2 tablespoons olive oil'),
  (23, '8-10 fresh basil leaves'),
  (23, 'Salt to taste');

-- Recipe 24: Keto Cauliflower Bowl
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (24, '1 large head cauliflower, cut into florets'),
  (24, '3 tablespoons olive oil'),
  (24, '1 teaspoon garlic powder'),
  (24, '1 teaspoon paprika'),
  (24, 'Salt and pepper to taste'),
  (24, '1 lb chicken thighs, boneless'),
  (24, '1 avocado, sliced'),
  (24, '1/2 cup sour cream'),
  (24, '1/4 cup shredded cheddar'),
  (24, '2 tablespoons chopped chives'),
  (24, '1 tablespoon lime juice');

-- Recipe 25: Shakshuka
INSERT INTO RECIPE_INGREDIENTS (RECIPE_ID, INGREDIENTS) VALUES
  (25, '2 tablespoons olive oil'),
  (25, '1 large onion, diced'),
  (25, '1 red bell pepper, diced'),
  (25, '4 cloves garlic, minced'),
  (25, '2 teaspoons cumin'),
  (25, '1 teaspoon paprika'),
  (25, '1/2 teaspoon cayenne'),
  (25, '1 (28-ounce) can crushed tomatoes'),
  (25, '6 eggs'),
  (25, '1/2 cup feta cheese, crumbled'),
  (25, '1/4 cup fresh parsley, chopped'),
  (25, 'Salt and pepper to taste'),
  (25, 'Crusty bread for serving');

-- ======================  RECIPE INSTRUCTIONS  =======================
-- Recipe 1
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (1, 'Bring a large pot of salted water to a boil. Cook pasta according to package directions.'),
  (1, 'Meanwhile, heat olive oil in a large skillet over medium heat. Add garlic and cook for 30 seconds, until fragrant.'),
  (1, 'Pour in crushed tomatoes, bring to a simmer, and cook for 5 minutes, stirring occasionally.'),
  (1, 'Drain pasta and add it to the sauce. Toss to combine.'),
  (1, 'Remove from heat and stir in Parmesan cheese and basil (if using). Season with salt and pepper to taste.'),
  (1, 'Serve immediately.');

-- Recipe 2
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (2, 'Whisk together eggs and milk in a bowl. Season with salt and pepper.'),
  (2, 'Heat butter in a skillet over medium heat. Pour in egg mixture.'),
  (2, 'As the eggs begin to cook, gently push cooked portions from the edge towards the center with a spatula, allowing the uncooked liquid to fill the empty space.'),
  (2, 'Continue cooking until eggs are scrambled to desired doneness.'),
  (2, 'Meanwhile, toast bread to your liking.'),
  (2, 'Serve scrambled eggs on toast and enjoy!');

-- Recipe 3
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (3, 'Preheat oven to 375 F (190 C). Line a baking sheet with parchment paper.'),
  (3, 'In a large bowl, mash black beans with a fork until slightly chunky.'),
  (3, 'Stir in cooked brown rice, rolled oats, red onion, bell pepper, garlic, olive oil, cilantro, chili powder, cumin, salt, and pepper.'),
  (3, 'Mix well until combined and the mixture holds its shape.'),
  (3, 'Form the mixture into 4 equal patties.'),
  (3, 'Place patties on the prepared baking sheet and bake for 20 minutes per side, or until golden brown and heated through.'),
  (3, 'Serve burgers on hamburger buns with your favorite toppings.');

-- Recipe 4
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (4, 'In a bowl, toss chicken with soy sauce and cornstarch. Set aside to marinate for 10 minutes.'),
  (4, 'Heat oil in a large skillet or wok over high heat. Add chicken and cook for 5-7 minutes, or until browned and cooked through.'),
  (4, 'Remove chicken from the pan and set aside.'),
  (4, 'Add bell peppers, broccoli, and onion to the pan. Cook for 3-4 minutes, or until softened slightly.'),
  (4, 'Stir in garlic and cook for another 30 seconds.'),
  (4, 'In a small bowl, whisk together chicken broth, sriracha, brown sugar, and sesame oil. Pour the sauce into the pan with the vegetables.'),
  (4, 'Add the cooked chicken back to the pan and toss to coat with the sauce.'),
  (4, 'Serve over cooked rice (optional) and enjoy!');

-- Recipe 5
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (5, 'In a glass or jar, layer half of the Greek yogurt.'),
  (5, 'Top with half of the granola and half of the berries.'),
  (5, 'Repeat layers with remaining yogurt, granola, and berries.'),
  (5, 'Drizzle with honey (optional) and serve immediately.');

-- Recipe 6
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (6, 'Heat olive oil in a large pot or Dutch oven over medium heat. Season chicken with oregano, garlic powder, salt, and pepper.'),
  (6, 'Add chicken to the pot and cook for 5-7 minutes, or until browned on all sides.'),
  (6, 'Stir in rice, diced tomatoes with their juices, chicken broth, and half of the parsley.'),
  (6, 'Bring to a boil, then reduce heat, cover, and simmer for 15 minutes, or until rice is cooked and fluffy.'),
  (6, 'Stir in remaining parsley and lemon slices. Serve immediately.');

-- Recipe 7
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (7, 'In a bowl, mix together tuna, mayonnaise, celery, onion powder, salt, and pepper.'),
  (7, 'Spread the tuna salad mixture onto one slice of bread.'),
  (7, 'Top with lettuce (optional) and the other slice of bread.'),
  (7, 'Cut the sandwich in half and enjoy!');

-- Recipe 8
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (8, 'In a large bowl, combine romaine lettuce, chicken, croutons, and Parmesan cheese.'),
  (8, 'Drizzle with Caesar salad dressing to taste and toss to coat.'),
  (8, 'Serve immediately.');

-- Recipe 9
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (9, 'Spread hummus evenly onto one tortilla.'),
  (9, 'Layer mixed greens, cucumber, bell pepper, and carrots on top of the hummus.'),
  (9, 'Sprinkle with feta cheese (optional).'),
  (9, 'Drizzle with a little olive oil (optional) for extra flavor.'),
  (9, 'Roll up the tortilla tightly and enjoy!');

-- Recipe 10
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (10, 'Reheat leftover soup according to package instructions.'),
  (10, 'In a bowl, combine mixed greens, cherry tomatoes, and feta cheese (optional).'),
  (10, 'Drizzle with vinaigrette dressing (optional) to taste.'),
  (10, 'Serve the salad alongside the reheated soup.');

-- Recipe 11
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (11, 'Mix together cream cheese, mayonnaise, melted mozzarella, and cheddar cheese.'),
  (11, 'Spread the mixture onto one slice of Italian bread.'),
  (11, 'Top with the other slice of bread.'),
  (11, 'Cut the sandwich in half and enjoy!');

-- Recipe 12
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (12, 'Swap sun-dried tomato pesto for basil pesto or your own homemade pesto.'),
  (12, 'Line a cookie sheet with foil for easy cleanup.'),
  (12, 'Add a dollop of sour cream or tomato basil pesto sauce if desired.');

-- Recipe 13
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (13, 'Mix shredded rotisserie chicken with sour cream and mayo sauce.'),
  (13, 'Add chopped pecans, sweet onion, and celery.'),
  (13, 'Serve in a sandwich or over lettuce.');

-- Recipe 14
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (14, 'Prepare cornbread/muffin mix according to package instructions.'),
  (14, 'Mix canned tuna with celery.'),
  (14, 'Top cornbread with tuna mixture and melted cheese.'),
  (14, 'Serve for a quick lunch or dinner.');

-- Recipe 15
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (15, 'Toss together baby spinach, deli smoked turkey, and crunchy walnuts.'),
  (15, 'Drizzle with maple syrup dressing.'),
  (15, 'Enjoy as a hearty lunch.');

-- Recipe 16
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (16, 'In a large bowl, combine soy sauce, brown sugar, honey, sesame oil, rice vinegar, garlic, ginger, and red pepper flakes. Marinate the sliced beef for at least 30 minutes, or up to overnight.'),
  (16, 'Heat vegetable oil in a large skillet or wok over high heat.'),
  (16, 'Add the beef and cook for 3-4 minutes, or until browned and cooked through.'),
  (16, 'Add bell pepper, onion, and broccoli florets to the pan and stir-fry for another 3-4 minutes, or until vegetables are tender-crisp.'),
  (16, 'Stir in cooked rice and heat through.'),
  (16, 'Serve garnished with sesame seeds (optional) and chopped green onions (optional).');

-- Recipe 17: Vegetable Pad Thai
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (17, 'Cook rice noodles according to package directions. Drain and set aside.'),
  (17, 'Heat oil in a large wok or skillet over high heat. Add garlic and tofu; stir-fry for 3 minutes.'),
  (17, 'Add mixed vegetables and stir-fry for 2 more minutes.'),
  (17, 'Push ingredients to one side of the pan. Pour beaten eggs (if using) into the empty space and scramble.'),
  (17, 'Add cooked noodles, soy sauce, tamarind paste, brown sugar, and lime juice. Toss everything together for 2 minutes.'),
  (17, 'Serve topped with crushed peanuts, green onions, and a lime wedge.');

-- Recipe 18: Chicken Tikka Masala
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (18, 'In a bowl, combine yogurt, lemon juice, 1 tablespoon garam masala, ginger, and 1 clove garlic. Add chicken and marinate for at least 1 hour (overnight preferred).'),
  (18, 'Heat oil in a large pot over medium heat. Add onion and cook until golden, about 5 minutes.'),
  (18, 'Add remaining garlic and cook for 30 seconds. Stir in tomato sauce, cream, paprika, remaining garam masala, and salt.'),
  (18, 'Add marinated chicken with the marinade. Bring to a simmer, cover, and cook on low for 45 minutes.'),
  (18, 'Garnish with fresh cilantro and serve with basmati rice or naan.');

-- Recipe 19: Sushi Rolls (Maki)
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (19, 'Rinse sushi rice until water runs clear. Combine rice and water in a rice cooker; cook until done.'),
  (19, 'Mix rice vinegar, sugar, and salt in a small bowl. Gently fold into cooked rice. Let cool to room temperature.'),
  (19, 'Place a nori sheet shiny side down on a bamboo sushi mat (or parchment paper).'),
  (19, 'Wet your hands and spread a thin layer of rice over the nori, leaving 1 inch uncovered at the top.'),
  (19, 'Lay cucumber, avocado, and fish in a line across the rice.'),
  (19, 'Roll the mat away from you, pressing firmly. Wet the uncovered nori edge and seal the roll.'),
  (19, 'Slice each roll into 6-8 pieces with a wet sharp knife. Serve with soy sauce, pickled ginger, and wasabi.');

-- Recipe 20: Falafel Pita
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (20, 'Pulse chickpeas, onion, garlic, parsley, flour, cumin, coriander, baking powder, salt, and pepper in a food processor until coarse paste forms. Refrigerate 1 hour.'),
  (20, 'Form mixture into 16 small patties.'),
  (20, 'Heat 2 inches of oil in a deep pot to 350 F. Fry falafel in batches for 3-4 minutes until golden brown.'),
  (20, 'Warm pitas in oven. Spread tahini sauce inside each pita.'),
  (20, 'Stuff with falafel, tomato slices, and lettuce. Serve immediately.');

-- Recipe 21: Hummus and Veggie Bowl
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (21, 'Cook quinoa or rice according to package directions. Let cool slightly.'),
  (21, 'In each serving bowl, layer 1/2 cup quinoa, 1/4 cup hummus, and arrange vegetables on top.'),
  (21, 'Whisk olive oil, lemon juice, oregano, salt, and pepper in a small bowl. Drizzle over bowls.'),
  (21, 'Garnish with fresh parsley and serve.');

-- Recipe 22: Bibimbap
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (22, 'Marinate beef in soy sauce, sesame oil, sugar, and garlic for 20 minutes.'),
  (22, 'In a hot skillet, saute zucchini with a pinch of salt for 2 minutes. Remove and set aside.'),
  (22, 'Saute carrots similarly for 2 minutes. Remove and set aside.'),
  (22, 'Saute spinach until wilted, about 1 minute. Remove and set aside.'),
  (22, 'Sear marinated beef in the same skillet for 2-3 minutes.'),
  (22, 'Fry eggs sunny-side up in a separate pan.'),
  (22, 'Divide rice between 4 bowls. Arrange beef and vegetables on top in separate sections. Place a fried egg in the center.'),
  (22, 'Serve with gochujang mixed in, topped with sesame seeds.');

-- Recipe 23: Margherita Pizza
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (23, 'Preheat oven to 500 F (260 C). If using a pizza stone, place it in the oven while preheating.'),
  (23, 'Roll out dough on a floured surface to a 12-inch circle.'),
  (23, 'Transfer dough to a pizza pan or parchment paper. Spread tomato sauce evenly, leaving a 1-inch border.'),
  (23, 'Top with mozzarella slices, Parmesan, and a drizzle of olive oil. Sprinkle with salt.'),
  (23, 'Bake for 10-12 minutes until crust is golden and cheese is bubbly.'),
  (23, 'Top with fresh basil leaves and serve immediately.');

-- Recipe 24: Keto Cauliflower Bowl
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (24, 'Preheat oven to 425 F. Toss cauliflower with 2 tablespoons olive oil, garlic powder, paprika, salt, and pepper.'),
  (24, 'Spread on a baking sheet and roast for 25 minutes until golden and tender.'),
  (24, 'Season chicken with salt and pepper. Heat remaining oil in a skillet and cook chicken 6-7 minutes per side until done. Slice.'),
  (24, 'Divide roasted cauliflower between 4 bowls. Top with sliced chicken, avocado, and cheddar.'),
  (24, 'Add a dollop of sour cream, sprinkle chives, and finish with lime juice.');

-- Recipe 25: Shakshuka
INSERT INTO RECIPE_INSTRUCTIONS (RECIPE_ID, INSTRUCTIONS) VALUES
  (25, 'Heat olive oil in a large oven-safe skillet over medium heat. Saute onion and bell pepper for 5 minutes until softened.'),
  (25, 'Add garlic, cumin, paprika, and cayenne. Cook for 1 minute until fragrant.'),
  (25, 'Pour in crushed tomatoes. Season with salt and pepper. Simmer for 10 minutes until sauce thickens.'),
  (25, 'Make 6 small wells in the sauce. Crack an egg into each well.'),
  (25, 'Cover and cook for 8-10 minutes until egg whites are set but yolks are still runny.'),
  (25, 'Sprinkle with feta and parsley. Serve hot with crusty bread.');

-- ======================  RECIPE_TAGS (many-to-many)  ================
-- Recipe 1: Cheesy Tomato Pasta [1,2,22,26,25]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (1,1),(1,2),(1,22),(1,26),(1,25);
-- Recipe 2: Scrambled Eggs [1,2,31,26,25]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (2,1),(2,2),(2,31),(2,26),(2,25);
-- Recipe 3: Black Bean Burgers [22,21,26,25]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (3,22),(3,21),(3,26),(3,25);
-- Recipe 4: Spicy Chicken Stir-Fry [31,25,10]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (4,31),(4,25),(4,10);
-- Recipe 5: Greek Yogurt Parfait [31,21,22,26]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (5,31),(5,21),(5,22),(5,26);
-- Recipe 6: One-Pot Lemon Garlic Chicken [31,10,26,25,29,27,1]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (6,31),(6,10),(6,26),(6,25),(6,29),(6,27),(6,1);
-- Recipe 7: Tuna Salad Sandwich [31,1,2,22]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (7,31),(7,1),(7,2),(7,22);
-- Recipe 8: Chicken Caesar Salad [1,2,31,27,29,26,25]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (8,1),(8,2),(8,31),(8,27),(8,29),(8,26),(8,25);
-- Recipe 9: Veggie Wraps [21,22,26,25,2]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (9,21),(9,22),(9,26),(9,25),(9,2);
-- Recipe 10: Leftover Soup and Salad [26,25,21,22,1]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (10,26),(10,25),(10,21),(10,22),(10,1);
-- Recipe 11: Ultimate Grilled Cheese [1,2]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (11,1),(11,2);
-- Recipe 12: Chicken Pesto Wraps [11,31,27,1,26,25]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (12,11),(12,31),(12,27),(12,1),(12,26),(12,25);
-- Recipe 13: Easy Chicken Salad [11,31,27,1,26,25]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (13,11),(13,31),(13,27),(13,1),(13,26),(13,25);
-- Recipe 14: Tuna Melt on Cornbread [31,1,2,30]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (14,31),(14,1),(14,2),(14,30);
-- Recipe 15: Turkey Spinach Salad [1,2,31,26,25]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (15,1),(15,2),(15,31),(15,26),(15,25);
-- Recipe 16: Korean Beef Bulgogi [26,25,31,10]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (16,26),(16,25),(16,31),(16,10);
-- Recipe 17: Vegetable Pad Thai [15,2,22,21]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (17,15),(17,2),(17,22),(17,21);
-- Recipe 18: Chicken Tikka Masala [13,9,31]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (18,13),(18,9),(18,31);
-- Recipe 19: Sushi Rolls (Maki) [16,30,8]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (19,16),(19,30),(19,8);
-- Recipe 20: Falafel Pita [19,6,21]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (20,19),(20,6),(20,21);
-- Recipe 21: Hummus and Veggie Bowl [18,21,1,23]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (21,18),(21,21),(21,1),(21,23);
-- Recipe 22: Bibimbap [20,5,31]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (22,20),(22,5),(22,31);
-- Recipe 23: Margherita Pizza [11,3,22,21]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (23,11),(23,3),(23,22),(23,21);
-- Recipe 24: Keto Cauliflower Bowl [27,4,29,31]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (24,27),(24,4),(24,29),(24,31);
-- Recipe 25: Shakshuka [18,5,22,26]
INSERT INTO RECIPE_TAGS (RECIPE_ID, TAG_ID) VALUES (25,18),(25,5),(25,22),(25,26);

-- ======================  RATINGS (sample)  ==========================
INSERT INTO RATING (ID, USER_ACCOUNT_ID, RECIPE_ID, RECIPE_RATING, LOCAL_DATE_TIME, REVIEW) VALUES
  (1,  3, 1,  5, '2024-02-10 12:00:00', 'Delicious and so easy on a weeknight.'),
  (2,  4, 1,  4, '2024-02-11 13:00:00', 'Tasty but a little bland for our taste.'),
  (3,  5, 2,  5, '2024-02-12 08:00:00', 'Perfect quick breakfast.'),
  (4,  3, 6,  5, '2024-02-13 19:00:00', 'Family favorite, one-pot win.'),
  (5,  4, 12, 4, '2024-02-14 12:30:00', 'Loved the pesto flavor.'),
  (6,  5, 17, 5, '2024-02-15 19:30:00', 'Better than takeout pad thai!'),
  (7,  3, 18, 4, '2024-02-16 20:00:00', 'Great with basmati rice.'),
  (8,  4, 22, 5, '2024-02-17 19:00:00', 'Amazing flavors in every bite.'),
  (9,  5, 23, 5, '2024-02-18 18:00:00', 'Perfect crust and toppings.'),
  (10, 3, 25, 4, '2024-02-19 10:00:00', 'Great brunch dish with crusty bread.');

-- ======================  SEQUENCE RESET  ============================
ALTER SEQUENCE tag_seq RESTART WITH 32;
ALTER SEQUENCE recipe_seq RESTART WITH 26;
ALTER SEQUENCE rating_seq RESTART WITH 11;
