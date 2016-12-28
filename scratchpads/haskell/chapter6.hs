multThree :: (Num a) => a -> a -> a -> a
multThree x y z = x * y * z



compareWithHundred :: (Num a, Ord a) => a -> Ordering
compareWithHundred = compare 100


divideByTen :: (Floating a) => a -> a
divideByTen = (/10)



isUpperAlphanum :: Char -> Bool
isUpperAlphanum = (`elem` ['A'..'Z'])


applyTwice :: (a -> a) -> a -> a
applyTwice f x = f (f x)


--applyTwice (+3) 10
--applyTwice (++ " HAHA") "HEY"
--applyTwice ("HAHA " ++) "HEY"
--applyTwice (multThree 2 2) 9
--applyTwice (3:) [1]


zipWith' :: (a -> b -> c) -> [a] -> [b] -> [c]
zipWith' _ [] _ = []
zipWith' _ _ [] = []
zipWith' f (x:xs) (y:ys) = f x y : zipWith' f xs ys


map' :: (a -> b) -> [a] -> [b]  
map' _ [] = []  
map' f (x:xs) = f x : map f xs  


--sum (takeWhile (<10000) (filter odd (map (^2) [1..])))



chain :: (Integral a) => a -> [a]
chain 1 = [1]
chain n
    | even n = n:chain(n `div` 2)
    | odd n  = n:chain(n*3 + 1)


numLongChains :: Int  
numLongChains = length (filter isLong (map chain [1..100]))  
    where isLong xs = length xs > 15



