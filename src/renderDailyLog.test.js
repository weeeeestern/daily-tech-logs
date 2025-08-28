const assert = require('assert');
const { renderDailyLog } = require('./renderDailyLog');

const template = `# {{date}}\n\n## {{category}}\n\n{{question}}\n\n{{answers}}\n`;

const output = renderDailyLog({
  date: '2024-06-01',
  category: 'Backend',
  question: 'What is dependency injection?',
  answers: [
    'A technique where an object receives its dependencies.',
    'Promotes decoupling and easier testing.'
  ]
}, template);

assert(output.includes('2024-06-01'));
assert(output.includes('Backend'));
assert(output.includes('- A technique where an object receives its dependencies.'));
assert(output.includes('- Promotes decoupling and easier testing.'));

console.log('renderDailyLog test passed');
