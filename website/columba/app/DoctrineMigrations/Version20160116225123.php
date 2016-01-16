<?php

namespace Application\Migrations;

use Doctrine\DBAL\Migrations\AbstractMigration;
use Doctrine\DBAL\Schema\Schema;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
class Version20160116225123 extends AbstractMigration
{
    /**
     * @param Schema $schema
     */
    public function up(Schema $schema)
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->abortIf($this->connection->getDatabasePlatform()->getName() != 'mysql', 'Migration can only be executed safely on \'mysql\'.');

        $this->addSql('CREATE TABLE oauth_access_token (id INT AUTO_INCREMENT NOT NULL, client_id INT NOT NULL, user_id INT DEFAULT NULL, token VARCHAR(255) NOT NULL, expires_at INT DEFAULT NULL, scope VARCHAR(255) DEFAULT NULL, UNIQUE INDEX UNIQ_F7FA86A45F37A13B (token), INDEX IDX_F7FA86A419EB6921 (client_id), INDEX IDX_F7FA86A4A76ED395 (user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ENGINE = InnoDB');
        $this->addSql('CREATE TABLE oauth_auth_code (id INT AUTO_INCREMENT NOT NULL, client_id INT NOT NULL, user_id INT DEFAULT NULL, token VARCHAR(255) NOT NULL, redirect_uri LONGTEXT NOT NULL, expires_at INT DEFAULT NULL, scope VARCHAR(255) DEFAULT NULL, UNIQUE INDEX UNIQ_4D12F0E05F37A13B (token), INDEX IDX_4D12F0E019EB6921 (client_id), INDEX IDX_4D12F0E0A76ED395 (user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ENGINE = InnoDB');
        $this->addSql('CREATE TABLE oauth_client (id INT AUTO_INCREMENT NOT NULL, random_id VARCHAR(255) NOT NULL, redirect_uris LONGTEXT NOT NULL COMMENT \'(DC2Type:array)\', secret VARCHAR(255) NOT NULL, allowed_grant_types LONGTEXT NOT NULL COMMENT \'(DC2Type:array)\', PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ENGINE = InnoDB');
        $this->addSql('CREATE TABLE oauth_refresh_token (id INT AUTO_INCREMENT NOT NULL, client_id INT NOT NULL, user_id INT DEFAULT NULL, token VARCHAR(255) NOT NULL, expires_at INT DEFAULT NULL, scope VARCHAR(255) DEFAULT NULL, UNIQUE INDEX UNIQ_55DCF7555F37A13B (token), INDEX IDX_55DCF75519EB6921 (client_id), INDEX IDX_55DCF755A76ED395 (user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ENGINE = InnoDB');
        $this->addSql('ALTER TABLE oauth_access_token ADD CONSTRAINT FK_F7FA86A419EB6921 FOREIGN KEY (client_id) REFERENCES oauth_client (id)');
        $this->addSql('ALTER TABLE oauth_access_token ADD CONSTRAINT FK_F7FA86A4A76ED395 FOREIGN KEY (user_id) REFERENCES fos_user (id)');
        $this->addSql('ALTER TABLE oauth_auth_code ADD CONSTRAINT FK_4D12F0E019EB6921 FOREIGN KEY (client_id) REFERENCES oauth_client (id)');
        $this->addSql('ALTER TABLE oauth_auth_code ADD CONSTRAINT FK_4D12F0E0A76ED395 FOREIGN KEY (user_id) REFERENCES fos_user (id)');
        $this->addSql('ALTER TABLE oauth_refresh_token ADD CONSTRAINT FK_55DCF75519EB6921 FOREIGN KEY (client_id) REFERENCES oauth_client (id)');
        $this->addSql('ALTER TABLE oauth_refresh_token ADD CONSTRAINT FK_55DCF755A76ED395 FOREIGN KEY (user_id) REFERENCES fos_user (id)');
        $this->addSql('ALTER TABLE fos_user ADD gcm_token VARCHAR(1000) DEFAULT NULL');
        $this->addSql('CREATE UNIQUE INDEX UNIQ_957A64796B01BC5B ON fos_user (phone_number)');
    }

    /**
     * @param Schema $schema
     */
    public function down(Schema $schema)
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->abortIf($this->connection->getDatabasePlatform()->getName() != 'mysql', 'Migration can only be executed safely on \'mysql\'.');

        $this->addSql('ALTER TABLE oauth_access_token DROP FOREIGN KEY FK_F7FA86A419EB6921');
        $this->addSql('ALTER TABLE oauth_auth_code DROP FOREIGN KEY FK_4D12F0E019EB6921');
        $this->addSql('ALTER TABLE oauth_refresh_token DROP FOREIGN KEY FK_55DCF75519EB6921');
        $this->addSql('DROP TABLE oauth_access_token');
        $this->addSql('DROP TABLE oauth_auth_code');
        $this->addSql('DROP TABLE oauth_client');
        $this->addSql('DROP TABLE oauth_refresh_token');
        $this->addSql('DROP INDEX UNIQ_957A64796B01BC5B ON fos_user');
        $this->addSql('ALTER TABLE fos_user DROP gcm_token');
    }
}
