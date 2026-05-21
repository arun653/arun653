You are an expert software architect and technical product manager.

Create a comprehensive Product Requirements Document (PRD) for a 
software product with the following details. Output it as a 
professional Word document (.docx) with a cover page, section 
headings, tables, and bullet points.

## Product Context

Name: ArchGuard AI
Tagline: Your Architecture, Always Honest

This is a VS Code-native intelligent architecture conformance 
platform for Java application modernization programs. It combines 
static code analysis, a self-learning framework knowledge graph, 
and AI-powered reasoning (via Claude API) to validate developer 
implementations against architect-defined HLD/LLD documents — 
surfacing deviations, coding standard violations, and design 
pattern mismatches directly inside the developer's IDE.

It runs entirely on source code and bytecode — no running 
application required. It is not a CI/CD tool. It runs on-demand 
inside VS Code using GitHub Copilot Agent Mode + a custom MCP 
server + a custom VS Code extension.

## Core Architecture (3 pillars)

1. Architect Intent Ingestion — Parse HLD/LLD documents 
   (PlantUML, Mermaid, draw.io XML, YAML rule files) into a 
   structured intent graph stored in Neo4j locally.

2. Code Reality Extraction — Static analysis of Java source and 
   bytecode to build a code graph (classes, dependencies, layers, 
   patterns) using JavaParser and jQAssistant.

3. AI-Powered Conformance Engine — Graph diff between intent 
   graph and code graph + Claude API reasoning to produce 
   actionable deviation reports with plain-English explanations 
   and fix suggestions.

## 6 Capability Areas to cover in detail

1. Architecture Conformance Validation
   - Component boundary validation
   - Layer violation detection
   - Module dependency rule enforcement
   - Microservice boundary detection
   - Missing component detection

2. Coding Standard Enforcement
   - Naming conventions, class/method size limits
   - Exception handling standards
   - Annotation correctness (Spring, JPA, etc.)
   - Injection style enforcement (constructor over field)
   - DTO/Entity separation
   - Custom YAML rule support

3. Design Pattern Detection and Validation
   - Structural pattern detection (Singleton, Factory, Builder, 
     Proxy, Decorator, Facade, Adapter)
   - Behavioural pattern detection (Strategy, Observer, Template 
     Method, Chain of Responsibility)
   - Spring-native pattern validation (Repository, CQRS, Events)
   - Pattern semantic correctness via Claude API
   - Anti-pattern detection (God classes, circular deps, anemic 
     domain models)

4. Framework Knowledge Base (Self-Learning)
   - Ingest any Java framework JAR (bytecode + source)
   - Claude API extracts usage rules, conventions, anti-patterns
   - Stored as queryable Neo4j graph
   - Requirement-to-pattern mapping 
     (e.g. "need pagination" → Pageable + Page<T>)
   - Version-aware — re-ingest new JAR to update
   - Works on Spring Boot, Hibernate, Quarkus, Micronaut, Kafka, 
     gRPC, and any Java framework

5. VS Code Native Experience
   - Copilot Agent Mode natural language queries
   - Custom VS Code panel with C4/PlantUML diagram rendering
   - Problems panel integration with click-to-navigate
   - On-save lightweight analysis
   - Command palette commands for all operations

6. Deviation Reporting
   - Severity: Critical, Major, Minor, Advisory
   - Plain-English explanation per deviation (Claude API)
   - Fix suggestions per deviation (Claude API)
   - Trend tracking (new vs existing deviations)
   - Export as HTML or Markdown

## Technology Stack

| Component          | Technology                        |
|--------------------|-----------------------------------|
| VS Code Extension  | TypeScript                        |
| MCP Server         | Node.js / Python                  |
| Code Parser        | JavaParser                        |
| Bytecode Analyser  | jQAssistant + ASM                 |
| Architecture Rules | ArchUnit                          |
| Coding Standards   | Checkstyle + PMD                  |
| Graph Database     | Neo4j (local, free)               |
| AI Reasoning       | Claude API (Anthropic)            |
| Diagram Rendering  | PlantUML VS Code Extension        |
| LLD Input Formats  | PlantUML / Mermaid / draw.io / YAML |

## Functional Requirements

Cover these 4 modules with numbered requirements (FR-01 onwards) 
and Priority column (Must Have / Should Have / Nice to Have):
- Architect Input Module (FR-01 to FR-08)
- Code Analysis Module (FR-09 to FR-16)
- Framework Knowledge Base Module (FR-17 to FR-23)
- Conformance Engine (FR-24 to FR-32)

## Non-Functional Requirements

Cover: Performance (scan times), Availability (offline), 
Security (no code sent externally except Claude API), 
Portability (Win/Mac/Linux), Usability (15 min onboarding), 
Maintainability (JAR re-ingestion only), Scalability (10k classes)

## Phased Delivery Roadmap

5 phases over 22 weeks:
- Phase 1 (Wk 1-4): Foundation — MCP server, JavaParser, Neo4j, 
  VS Code extension skeleton, ArchUnit layer detection
- Phase 2 (Wk 5-8): Architect Intent Ingestion — PlantUML, 
  Mermaid, YAML parsers, intent graph, first deviation report
- Phase 3 (Wk 9-13): Framework Knowledge Base — JAR ingestion, 
  Claude API integration, requirement-to-pattern mapping
- Phase 4 (Wk 14-17): Design Patterns & Coding Standards — AST 
  pattern detection, semantic correctness, Checkstyle/PMD
- Phase 5 (Wk 18-22): Full VS Code Experience — diagram panel, 
  Problems panel, Agent Mode queries, export

## Value Proposition

Cover:
1. Problems Solved table (8 rows: problem + how solved)
2. Business Value (6 bullet points)
3. Differentiator table vs existing tools 
   (Lattix, manual review, no-tool state) across:
   Architecture conformance, Framework rule learning, 
   Design pattern validation, Developer experience, 
   LLD comparison, Natural language queries, Fix suggestions

## Constraints & Assumptions

Constraints:
- Java only in v1.0
- Requires local Neo4j (Community Edition — free)
- Claude API needs internet + Anthropic API key
- Narrative LLD text extraction ~75-80% accuracy; 
  PlantUML/YAML strongly preferred

Assumptions:
- Architects adopt PlantUML or YAML as standard LLD input
- Developers have VS Code + GitHub Copilot paid plan
- Maven or Gradle build systems
- Neo4j Desktop installable on developer machines
- Teams have Anthropic API keys

## Document Structure Required

1. Cover Page — product name, tagline, PRD title, version, date, 
   classification, powered-by line
2. Executive Summary
3. Problem Statement (with pain points table)
4. Solution Overview (with identity table)
5. Capabilities (6 sections in detail)
6. Functional Requirements (4 modules, numbered table with priority)
7. VS Code Integration Requirements
8. Non-Functional Requirements (table)
9. Technology Stack (table)
10. Phased Delivery Roadmap (5 phases)
11. Value Proposition (3 sub-sections)
12. Constraints & Assumptions
13. Glossary (key terms: Architecture Drift, HLD, LLD, MCP, 
    Intent Graph, Code Graph, Static Analysis, Framework Knowledge 
    Base, Conformance Engine, Deviation, AST)

## Formatting Requirements

- Professional Word document (.docx)
- Dark navy headings (#1E3A5F for H1, #2E75B6 for H2)
- All tables with dark navy header rows and alternating row shading
- Cover page centered with divider line
- Section dividers between major sections
- Arial font throughout
- US Letter page size
- Version: 1.0 | Date: [current date]
- Footer: ArchGuard AI — PRD v1.0 — Confidential