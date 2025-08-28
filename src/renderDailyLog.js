const DEFAULT_TEMPLATE = `# Daily Tech Log - {{date}}

## Category: {{category}}

### Question
{{question}}

### Answers
{{answers}}
`;

/**
 * Render a daily log markdown entry.
 *
 * @param {Object} data - Structured data for the log.
 * @param {string} data.date - ISO date string for the entry.
 * @param {string} data.category - Category of the question.
 * @param {string} data.question - The question being answered.
 * @param {string[]} [data.answers] - Array of answers or notes.
 * @param {Object} [data.metadata] - Additional placeholders and values.
 * @param {string} [template] - Optional template string with {{placeholders}}.
 * @returns {string} Rendered markdown string.
 */
function renderDailyLog(data, template = DEFAULT_TEMPLATE) {
  const { date = '', category = '', question = '', answers = [], metadata = {} } = data || {};
  const answerLines = Array.isArray(answers) ? answers.map(a => `- ${a}`).join('\n') : '';

  const placeholders = {
    date,
    category,
    question,
    answers: answerLines,
    ...metadata,
  };

  return template.replace(/\{\{(\w+)\}\}/g, (_, key) => {
    return Object.prototype.hasOwnProperty.call(placeholders, key) ? placeholders[key] : '';
  });
}

module.exports = { renderDailyLog, DEFAULT_TEMPLATE };
