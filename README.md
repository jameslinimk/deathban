# Deathban

Custom plugin for [Zerosmp](https://discord.gg/D8EyC4dUYv) that adds a deathban to the server

## How it works

When a player dies, they lose lives. When they run out of lives, they are banned from the server for a set amount of
time.

- When a player loses lives, they come back after a set period
- Players lose more lives when killed by another player
- Uses the [AdvancedBan](https://www.spigotmc.org/resources/advancedban.8695/) api to ban players

## Usage

- `/lives` - Shows the player how many lives they have left
- `/deaths` - Shows the player how many times they have died and information about their deaths
- `/editlives <player> <amount>` - Edits the lives of a player (requires `deathban.editlives` permission)

## Defalt Config

```yaml
# The limit of lives a player can have. Player lives reset/default to this
livesLimit: 4

# The amount of time until a player's lives are reset
updateInterval: 24

# The amount of time a player will be banned for once they run out of lives
banLength: 24

# The reason passed into AdvancedBan when a player is banned
banReason: "@deathban"

# How many lives a player will lose when they die by a player
playerDeathCost: 2

# How many lives a player will lose when they die by a non-player
nonPlayerDeathCost: 1
```

### Feel free to use this plugin on your server but give credit
