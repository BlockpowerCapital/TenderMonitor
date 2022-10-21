# TenderMonitor
TenderMonitor is a tool for monitoring Cosmos nodes. When the node is down or the block time is delayed, it will be notified through Telegram Bot. All chains using TenderMint Core can be included in monitoring while provided the Node API is enabled.

Instruction Steps :
1. Enable the API interface in the `config/app.toml` file
2. Configure the DATA path, Telegram Bot Token, Name and reg code in application.properties
3. Add Server configuration information into the DATA/Server path
4. Start TenderMonitor with `java -jar TenderMonitor-{version}.jar -Xms512MB -Xmx1024MB`
5. echo `/reg REGCODE` to Telegram Bot.


Then, it will be ready to start the alert.
