// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.core.tokens.NameToken;
import com.google.codeu.mathlang.core.tokens.NumberToken;
import com.google.codeu.mathlang.core.tokens.StringToken;
import com.google.codeu.mathlang.core.tokens.SymbolToken;
import com.google.codeu.mathlang.parsing.TokenReader;

import java.lang.Character;
import java.lang.Double;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {

  private String source;
  private int index;

  public MyTokenReader(String source) {
	index = 0;
	this.source = source;
  }

  private SymbolToken getSymbolToken() {
    char symbol = source.charAt(index);
	index++;
	return new SymbolToken(symbol);
  }

  private NumberToken getNumberToken() throws IOException {
    int start = index;
    while (!(index >= source.length()) && Character.isDigit(source.charAt(index))) {
      index++;
    }
    if (!(index >= source.length()) && source.charAt(index) == '.') {
      index++;
    }
    while (!(index >= source.length()) && Character.isDigit(source.charAt(index))) {
      index++;
    }

    String str = source.substring(start, index);
    return str.length() == 0 ? null : new NumberToken(Double.parseDouble(str));
  }

  private StringToken getStringToken() throws IOException {
    index++;
    int start = index;
    while (!(index >= source.length()) && source.charAt(index) != '\"') {
      if (source.charAt(index) == '\n') {
        throw new IOException();
      }
      index++;
    }
    if ((index >= source.length())) {
      return null;
    }
    String str = source.substring(start, index);
    index++;
    return new StringToken(str);
  }
  
  private NameToken getNameToken() {
    int start = index;
	while (!(index >= source.length()) && Character.isAlphabetic(source.charAt(index))) {
	  index++;
	}
	String str = source.substring(start, index);
	return str.length() == 0 ? null : new NameToken(str);
  }
  
  private boolean isSymbol(char c) {
    return c == '=' || c == '+' || c == '-' || c == ';';
  }

  private Token getToken() throws IOException {
	Token token = null;
	char start = source.charAt(index);
    if (Character.isLetter(start)) {
      token = getNameToken();
    } else if (Character.isDigit(start)) {
      token = getNumberToken();
    } else if (isSymbol(start)) {
      token = getSymbolToken();
    } else if (start == '\"') {
      token = getStringToken(); 
    } else {
      throw new IOException();
    }
    return token;
  }

  @Override
  public Token next() throws IOException {
    source = source.trim();
    if (index >= source.length())
      return null;
    return getToken();
  }
}
