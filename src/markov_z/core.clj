(ns markov-z.core)

(def moves
  [:forward
   :back
   :up
   :down
   :left
   :right])

(def movement-matrix

        ;; Forward Back Up Down Left Right
  {:forward [0.5 0.1 0.1 0.1 0.1 0.1]
   :back    [0.1 0.2 0.2 0.2 0.15 0.15]
   :up      [0.3 0.15 0.3 0.05 0.05 0.15]
   :down    [0.2 0.15 0.05 0.2 0.2 0.2]
   :left    [0.2 0.15 0.2 0.05 0.2 0.2]
   :right   [0.4 0.2 0.1 0.1 0.1 0.1]})

(def movement-directions
  {:forward [1 0 0]
   :back    [-1 0 0]
   :up      [0 0 1]
   :down    [0 0 -1]
   :left    [0 -1 0]
   :right   [0 1 0]})

(def agent-start {:position [0 0 0] :prev-move :forward})

(defn weighted-rand
  [weights]
  (let [total (reduce + weights)
        r     (rand total)]
    (loop [i 0 sum 0]
      (if (< r (+ (weights i) sum))
        i
        (recur (inc i) (+ (weights i) sum))))))

(defn step [agent remaining]
  (println "Position:        " (agent :position))
  (println "Previous Move:   " (agent :prev-move))
  (println "Steps Remaining: " remaining)
  (println)
  
  (if (pos? remaining)
    (let [movement-probabilities (movement-matrix (agent :prev-move))
          next-move (moves (weighted-rand movement-probabilities))
          next-move-directions (movement-directions next-move)]
      (recur (-> agent
                 (update :prev-move (fn [_] next-move))
                 (update :position (fn [old-pos]
                                     (into [] (for [i (range (count old-pos))]
                                                (+ (old-pos i) (next-move-directions i)))))))
             (dec remaining)))))

(defn -main [& args]
  (step agent-start 5))

















