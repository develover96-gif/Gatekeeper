package com.example.data.model

import kotlin.random.Random

data class MathProblem(
  val prompt: String,
  val solution: String,
  val difficultyLabel: String,
  val problemType: String, // "ARITHMETIC", "SEQUENCE", "EQUATION_BALANCE", "LOGIC_RIDDLE"
  val hint: String,
  val explanation: String
)

object ProblemSolvingChallengeGenerator {

  fun generate(
    difficultyTier: String = "STANDARD",
    problemType: String = "ANY"
  ): MathProblem {
    return when (difficultyTier.uppercase()) {
      "GENTLE" -> generateGentleArithmetic()
      "ADVANCED", "FORTIFIED" -> generateAdvancedArithmetic()
      "PROBLEM_SOLVING" -> generateProblemSolving(problemType)
      else -> generateStandardArithmetic()
    }
  }

  fun generateByLevel(level: Int, problemType: String = "ANY"): MathProblem {
    return when (level) {
      1 -> generateGentleArithmetic()
      3 -> generateAdvancedArithmetic()
      4 -> generateProblemSolving(problemType)
      else -> generateStandardArithmetic()
    }
  }

  // Level 1: Gentle arithmetic (Dual digit addition/subtraction)
  private fun generateGentleArithmetic(): MathProblem {
    val isAddition = Random.nextBoolean()
    return if (isAddition) {
      val a = Random.nextInt(12, 49)
      val b = Random.nextInt(11, 48)
      val ans = a + b
      MathProblem(
        prompt = "$a + $b",
        solution = ans.toString(),
        difficultyLabel = "Gentle (Level 1)",
        problemType = "ARITHMETIC",
        hint = "Add the tens first: ${a / 10 * 10} + ${b / 10 * 10} = ${(a / 10 + b / 10) * 10}, then add units.",
        explanation = "$a + $b = $ans"
      )
    } else {
      val a = Random.nextInt(45, 99)
      val b = Random.nextInt(14, a - 10)
      val ans = a - b
      MathProblem(
        prompt = "$a - $b",
        solution = ans.toString(),
        difficultyLabel = "Gentle (Level 1)",
        problemType = "ARITHMETIC",
        hint = "Subtract ${b / 10 * 10} first, then subtract ${b % 10}.",
        explanation = "$a - $b = $ans"
      )
    }
  }

  // Level 2: Standard arithmetic (Multiplication + Addition/Subtraction)
  private fun generateStandardArithmetic(): MathProblem {
    val variant = Random.nextInt(3)
    return when (variant) {
      0 -> {
        val a = Random.nextInt(6, 12)
        val b = Random.nextInt(6, 10)
        val c = Random.nextInt(11, 35)
        val ans = (a * b) + c
        MathProblem(
          prompt = "$a × $b + $c",
          solution = ans.toString(),
          difficultyLabel = "Standard (Level 2)",
          problemType = "ARITHMETIC",
          hint = "Calculate $a × $b first (= ${a * b}), then add $c.",
          explanation = "($a × $b) + $c = ${a * b} + $c = $ans"
        )
      }
      1 -> {
        val a = Random.nextInt(7, 12)
        val b = Random.nextInt(6, 9)
        val prod = a * b
        val c = Random.nextInt(10, prod - 10)
        val ans = prod - c
        MathProblem(
          prompt = "$a × $b - $c",
          solution = ans.toString(),
          difficultyLabel = "Standard (Level 2)",
          problemType = "ARITHMETIC",
          hint = "$a × $b = $prod. Now subtract $c.",
          explanation = "($a × $b) - $c = $prod - $c = $ans"
        )
      }
      else -> {
        val mult = Random.nextInt(6, 10)
        val factor = Random.nextInt(6, 12)
        val dividend = mult * factor
        val add = Random.nextInt(12, 38)
        val ans = factor + add
        MathProblem(
          prompt = "$dividend ÷ $mult + $add",
          solution = ans.toString(),
          difficultyLabel = "Standard (Level 2)",
          problemType = "ARITHMETIC",
          hint = "$dividend ÷ $mult = $factor. Then add $add.",
          explanation = "($dividend ÷ $mult) + $add = $factor + $add = $ans"
        )
      }
    }
  }

  // Level 3: Advanced multi-step arithmetic with brackets
  private fun generateAdvancedArithmetic(): MathProblem {
    val variant = Random.nextInt(2)
    return if (variant == 0) {
      val a = Random.nextInt(12, 18)
      val b = Random.nextInt(4, 7)
      val prod = a * b
      val c = Random.nextInt(15, 39)
      val ans = prod - c
      MathProblem(
        prompt = "($a × $b) - $c",
        solution = ans.toString(),
        difficultyLabel = "Advanced (Level 3)",
        problemType = "ARITHMETIC",
        hint = "$a × $b = $prod. Now subtract $c.",
        explanation = "($a × $b) - $c = $prod - $c = $ans"
      )
    } else {
      val a = Random.nextInt(14, 22)
      val b = Random.nextInt(3, 5)
      val c = Random.nextInt(16, 32)
      val ans = (a * b) + c
      MathProblem(
        prompt = "($a × $b) + $c",
        solution = ans.toString(),
        difficultyLabel = "Advanced (Level 3)",
        problemType = "ARITHMETIC",
        hint = "First evaluate parentheses: $a × $b = ${a * b}. Then add $c.",
        explanation = "($a × $b) + $c = ${a * b} + $c = $ans"
      )
    }
  }

  // Level 4: Problem Solving & Logic (Sequences, Balance Equations, Logic Riddles)
  private fun generateProblemSolving(type: String): MathProblem {
    val choices = if (type == "ANY" || type.isBlank()) {
      listOf("SEQUENCES", "BALANCE", "LOGIC_RIDDLES")
    } else {
      listOf(type)
    }

    return when (choices.random()) {
      "SEQUENCES" -> generateSequencePattern()
      "BALANCE" -> generateEquationBalance()
      else -> generateLogicRiddle()
    }
  }

  private fun generateSequencePattern(): MathProblem {
    val templates = listOf(
      // Multiply by 2 and add 1
      Triple(
        "Find next in sequence: 3, 7, 15, 31, ?",
        "63",
        "Each term doubles and adds 1: 31 × 2 + 1"
      ),
      Triple(
        "Find next in sequence: 2, 6, 18, 54, ?",
        "162",
        "Each term is multiplied by 3: 54 × 3"
      ),
      Triple(
        "Find next in sequence: 4, 9, 19, 39, ?",
        "79",
        "Pattern is × 2 + 1: 39 × 2 + 1"
      ),
      Triple(
        "Find next square: 1, 4, 9, 16, 25, ?",
        "36",
        "Consecutive squares: 6 × 6"
      ),
      Triple(
        "Find next in sequence: 5, 8, 14, 23, ?",
        "35",
        "Differences increase by 3: +3, +6, +9, so next is +12"
      ),
      Triple(
        "Find next in sequence: 100, 91, 83, 76, ?",
        "70",
        "Decreasing subtractions: -9, -8, -7, so next is -6"
      ),
      Triple(
        "Find next in sequence: 1, 2, 4, 8, 16, ?",
        "32",
        "Powers of 2: 16 × 2"
      )
    )

    val item = templates.random()
    return MathProblem(
      prompt = item.first,
      solution = item.second,
      difficultyLabel = "Problem Solving: Sequence",
      problemType = "SEQUENCE",
      hint = item.third,
      explanation = "${item.first} -> Answer is ${item.second}. Rationale: ${item.third}."
    )
  }

  private fun generateEquationBalance(): MathProblem {
    val templates = listOf(
      Triple(
        "Solve for ?: ? × 7 + 15 = 57",
        "6",
        "Subtract 15 from 57 (= 42), then divide by 7."
      ),
      Triple(
        "Solve for ?: 48 - (? × 6) = 12",
        "6",
        "48 - 12 = 36. Now 36 ÷ 6 = ?"
      ),
      Triple(
        "Solve for ?: ? × 9 - 18 = 45",
        "7",
        "Add 18 to 45 (= 63), then divide by 9."
      ),
      Triple(
        "Solve for ?: (60 ÷ ?) + 18 = 28",
        "6",
        "60 ÷ ? = 10, so ? = 6."
      ),
      Triple(
        "Solve for ?: ? × 8 + 14 = 70",
        "7",
        "70 - 14 = 56. 56 ÷ 8 = ?"
      ),
      Triple(
        "Solve for ?: 75 - (? × 8) = 27",
        "6",
        "75 - 27 = 48. 48 ÷ 8 = ?"
      )
    )

    val item = templates.random()
    return MathProblem(
      prompt = item.first,
      solution = item.second,
      difficultyLabel = "Problem Solving: Equation Balance",
      problemType = "EQUATION_BALANCE",
      hint = item.third,
      explanation = "${item.first} -> ? = ${item.second}."
    )
  }

  private fun generateLogicRiddle(): MathProblem {
    val riddles = listOf(
      Triple(
        "A runner runs 6 miles at 8 min/mile, then walks 12 min. Total minutes?",
        "60",
        "6 miles × 8 min = 48 min. 48 + 12 = 60."
      ),
      Triple(
        "A train has 4 cars with 20 seats each and 2 cars with 30 seats. Total seats?",
        "140",
        "4 × 20 = 80 seats, 2 × 30 = 60 seats. 80 + 60 = 140."
      ),
      Triple(
        "Sum of two numbers is 40, their difference is 10. What is the larger number?",
        "25",
        "(40 + 10) ÷ 2 = 25 (the numbers are 25 and 15)."
      ),
      Triple(
        "3 shirts and 1 hat cost $55. The hat costs $10. What is the cost of 1 shirt?",
        "15",
        "$55 - $10 = $45 for 3 shirts. $45 ÷ 3 = $15."
      ),
      Triple(
        "A book has 120 pages. You read 30 pages daily for 3 days. Pages left?",
        "30",
        "3 days × 30 pages = 90 pages read. 120 - 90 = 30 pages left."
      ),
      Triple(
        "A parking lot has 8 rows of 15 spots. 35 spots are empty. How many cars are parked?",
        "85",
        "8 × 15 = 120 total spots. 120 - 35 = 85 parked cars."
      )
    )

    val item = riddles.random()
    return MathProblem(
      prompt = item.first,
      solution = item.second,
      difficultyLabel = "Problem Solving: Logic Riddle",
      problemType = "LOGIC_RIDDLE",
      hint = item.third,
      explanation = "${item.first} -> ${item.second}. Calculation: ${item.third}"
    )
  }
}
