package de.kaufhof.pillar

import java.util.Date

import com.datastax.driver.core.Session

class ReportingMigrator(reporter: Reporter, wrapped: Migrator) extends Migrator {
  override def initialize(session: Session, keyspace: String, replicationStrategy: ReplicationStrategy) {
    createKeyspace(session, keyspace, replicationStrategy)
    createMigrationsTable(session, keyspace)
  }

  override def migrate(session: Session, dateRestriction: Option[Date] = None) {
    reporter.migrating(session, dateRestriction)
    wrapped.migrate(session, dateRestriction)
  }

  override def destroy(session: Session, keyspace: String) {
    reporter.destroying(session, keyspace)
    wrapped.destroy(session, keyspace)
  }

  override def createKeyspace(session: Session, keyspace: String, replicationStrategy: ReplicationStrategy): Unit = {
    reporter.creatingKeyspace(session, keyspace, replicationStrategy)
    wrapped.createKeyspace(session, keyspace, replicationStrategy)
  }

  override def createMigrationsTable(session: Session, keyspace: String): Unit = {
    reporter.creatingMigrationsTable(session, keyspace)
    wrapped.createMigrationsTable(session, keyspace)
  }
}