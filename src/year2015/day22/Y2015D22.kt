package year2015.day22

import arrow.core.mapNotNull
import arrow.optics.*
import framework.InputProvider
import framework.solution
import kotlin.math.max
import kotlin.math.min

enum class Spell(val cost: Int, val action: Action) {
    MAGIC_MISSILE(53, Action.Damage(4)),
    DRAIN(73, Action.Drain(2)),
    SHIELD(113, Action.StartEffect(Effect.SHIELD)) {
        override fun canUse(state: State) = super.canUse(state) && !state.effects.contains(Effect.SHIELD)
    },
    POISON(173, Action.StartEffect(Effect.POISON)) {
        override fun canUse(state: State) = super.canUse(state) && !state.effects.contains(Effect.POISON)
    },
    RECHARGE(229, Action.StartEffect(Effect.RECHARGE)) {
        override fun canUse(state: State) = super.canUse(state) && !state.effects.contains(Effect.RECHARGE)
    };

    open fun canUse(state: State) = state.wizard.mana >= cost
}

enum class Effect(val lastFor: Int, val action: Action) {
    POISON(6, Action.Damage(3)),
    RECHARGE(5, Action.RestoreMana(101)),
    SHIELD(6, Action.Shield(7)),
}

sealed class Action {

    abstract fun applyToState(state: State): State

    data class Damage(val amount: Int): Action() {
        override fun applyToState(state: State): State =
            State.monster.health.modify(state) {
                it.damage(amount)
            }
    }

    data class Drain(val amount: Int): Action() {
        override fun applyToState(state: State): State =
            state.copy {
                State.monster.health transform { it.damage(amount) }
                State.wizard.health transform  { it.heal(amount) }
            }
    }

    data class RestoreMana(val amount: Int): Action() {
        override fun applyToState(state: State): State =
            State.wizard.mana.modify(state) { it + amount }
    }

    data class StartEffect(val effect: Effect): Action() {
        override fun applyToState(state: State): State {
            assert(!state.effects.contains(effect)) { "Tried to add an effect that is already active" }
            return State.effects.modify(state) { it + (effect to effect.lastFor) }
        }
    }

    data class Shield(val amount: Int): Action() {
        override fun applyToState(state: State): State =
            State.shield.modify(state) { amount }
    }
}


@optics data class Health(val maxHealth: Int, val currentHealth: Int = maxHealth) {

    fun damage(amount: Int) = Health.currentHealth.modify(this) {
        max(0, it - amount)
    }

    fun heal(amount: Int) = Health.currentHealth.modify(this) {
        min(maxHealth, it + amount)
    }

    fun isDead() = currentHealth == 0

    companion object
}


@optics data class Wizard(val health: Health, val mana: Int, val usedMana: Int = 0) {
    companion object
}


@optics data class Monster(val health: Health, val damage: Int) {
    companion object
}


@optics data class State(
    val wizard: Wizard,
    val monster: Monster,
    val hardMode: Boolean = false,
    val effects: Map<Effect, Int> = mapOf(),
    val shield: Int = 0,
) {

    private fun isOver() = wizard.health.isDead() || monster.health.isDead() || availableSpells().isEmpty()

    private fun availableSpells() = Spell.entries.filter {
        it.canUse(this)
    }

    private fun applyEffects(): State {

        val removeShield = State.shield.modify(this) { 0 }

        val afterApply = effects.keys.fold(removeShield) { state, effect ->
            effect.action.applyToState(state)
        }

        return State.effects.modify(afterApply) {
            it.mapNotNull { (_, lasts) ->
                assert(lasts >= 1)
                if (lasts == 1) null else lasts - 1
            }
        }
    }


    private fun doPlayerTurn(spell: Spell): State {
        assert(spell.canUse(this))

        val usedSpell = copy {
            State.wizard transform { wizard ->
                wizard.copy {
                    Wizard.mana transform { it - spell.cost }
                    Wizard.usedMana transform { it + spell.cost }
                }
            }
        }

        return spell.action.applyToState(usedSpell)
    }

    private fun ifNotOver(transform: (State) -> State) = if (isOver()) {
        this
    } else {
        transform(this)
    }

    private fun doMonsterTurn(): State {

        val damageAmount = max(1, monster.damage - shield)

        return State.wizard.health.modify(this) { it.damage(damageAmount) }
    }

    private fun hardModeDamage() = if (hardMode) {
        State.wizard.health.modify(this) { it.damage(1) }
    } else {
        this
    }

    fun leastAmountOfMana(): Int? {
        val memo = mutableMapOf<State, Int?>()

        fun findLeastMana(state: State, manaSpent: Int, currentMin: Int?): Int? {
            memo[state]?.let { return it }

            val afterEffects = state.applyEffects().hardModeDamage()

            if (afterEffects.wizard.health.isDead()) return null

            if (afterEffects.monster.health.isDead()) return manaSpent

            if (currentMin != null && manaSpent >= currentMin) return null

            val availableSpells = afterEffects.availableSpells()
            if (availableSpells.isEmpty()) return null

            val newMin = availableSpells.mapNotNull { spell ->
                val afterPlayerTurn = afterEffects.doPlayerTurn(spell)
                val afterMonsterTurn = afterPlayerTurn.applyEffects().ifNotOver { it.doMonsterTurn() }
                if (!afterMonsterTurn.wizard.health.isDead()) {
                    findLeastMana(afterMonsterTurn, manaSpent + spell.cost, currentMin)
                } else {
                    null
                }
            }.minOrNull()

            memo[state] = newMin
            return newMin
        }

        return findLeastMana(this, 0, null)
    }

    companion object
}

fun main() = solution(2015, 22, "Wizard Simulator 20XX") {

    fun InputProvider.parseMonster(): Monster {
        return Monster(
            Health(lines[0].substringAfter(": ").toInt()),
            lines[1].substringAfter(": ").toInt(),
        )
    }

    partOne {
        val wizard = Wizard(Health(50), 500)
        val monster = parseMonster()
        val state = State(wizard, monster)
        state.leastAmountOfMana()
    }
    
    partTwo {
        val wizard = Wizard(Health(50), 500)
        val monster = parseMonster()
        val state = State(wizard, monster, true)
        state.leastAmountOfMana()
    }
}