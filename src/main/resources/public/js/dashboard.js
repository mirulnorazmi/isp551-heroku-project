function getColor(labelString) {
  if (labelString) {
    const string = labelString.replace(/ /g, '');
    let hash = 0;
    let i;

    /* eslint-disable no-bitwise */
    for (i = 0; i < string.length; i += 1) {
      hash = string.charCodeAt(i) + ((hash << 5) - hash);
    }

    let colour = '';

    for (i = 0; i < 3; i += 1) {
      const value = (hash >> (i * 8)) & 0xff;
      colour += `00${value.toString(16)}`.substr(-2);
    }
    /* eslint-enable no-bitwise */

    if (parseInt(colour, 40) > 15658734) return '#eeeeee';
    return `#${colour}`;
  }
}


