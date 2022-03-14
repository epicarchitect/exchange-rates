package employcity.exchangerates.app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import employcity.exchangerates.app.api.RequestErrorType
import employcity.exchangerates.app.viewmodel.CurrenciesViewModel


@Composable
fun CurrenciesScreen(viewModel: CurrenciesViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(state.isLoading),
        onRefresh = { viewModel.refresh() },
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (error, list, filters) = createRefs()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(error) {
                            top.linkTo(parent.top)
                        }
                ) {
                    if (state.fetchError != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    imageVector = Icons.Default.Info,
                                    contentDescription = ""
                                )

                                Text(
                                    text = when (state.fetchError) {
                                        RequestErrorType.CONNECTION_ERROR -> "Connection error"
                                        RequestErrorType.NO_INTERNET -> "No internet"
                                        RequestErrorType.BAD_REQUEST -> "Bad request"
                                        RequestErrorType.NOT_AUTHORIZED -> "Not authorized"
                                        RequestErrorType.RATE_LIMIT_EXCEEDED -> "Rate limit exceeded"
                                        RequestErrorType.UNKNOWN_HTTP_ERROR -> "Unknown HTTP error"
                                        else -> "Unknown error"
                                    }
                                )

                                TextButton(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    onClick = { viewModel.refresh() }
                                ) {
                                    Text(text = "Refresh")
                                }
                            }

                            Divider()
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(list) {
                            height = Dimension.fillToConstraints
                            top.linkTo(error.bottom)
                            bottom.linkTo(filters.top)
                        },
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(state.currencies) {
                        CurrencyCard(it, viewModel)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(filters) {
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Column {
                        Divider(
                            modifier = Modifier.fillMaxWidth()
                        )

                        Filters(state.filter, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyCard(
    item: employcity.exchangerates.data.Currency,
    viewModel: CurrenciesViewModel
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.padding(end = 50.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${item.value} ${item.code}",
                    style = MaterialTheme.typography.subtitle1
                )
            }

            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable {
                        viewModel.setFavoriteCurrency(item.code, !item.isFavorite)
                    },
                imageVector = Icons.Filled.Star,
                tint = if (item.isFavorite) {
                    MaterialTheme.colors.secondary
                } else {
                    MaterialTheme.colors.onBackground
                },
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun Filters(
    filter: CurrenciesViewModel.Filter,
    viewModel: CurrenciesViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    if (expanded) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                onClick = { expanded = false }
            ) {
                Text("Hide filters")
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                text = "Base currency",
                style = MaterialTheme.typography.overline
            )
            BaseCurrencySelection(filter, viewModel)

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                text = "Sort type",
                style = MaterialTheme.typography.overline
            )
            SortTypeSelection(filter, viewModel)

            ShowOnlyFavoritesSwitch(filter, viewModel)
        }
    } else {
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            onClick = { expanded = true }
        ) {
            Text("Show filters")
        }
    }
}

@Composable
private fun BaseCurrencySelection(
    filter: CurrenciesViewModel.Filter,
    viewModel: CurrenciesViewModel
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        var dropdownMenuExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    dropdownMenuExpanded = !dropdownMenuExpanded
                }
        ) {
            CurrencySelectionItem(
                modifier = Modifier
                    .padding(end = 40.dp)
                    .align(Alignment.CenterStart),
                name = filter.baseCurrency?.name ?: "Loading...",
                code = filter.baseCurrency?.code ?: "Loading..."
            )

            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = ""
            )
        }

        DropdownMenu(
            expanded = dropdownMenuExpanded,
            onDismissRequest = {
                dropdownMenuExpanded = false
            }
        ) {
            filter.availableCurrenciesForBase.forEach {
                DropdownMenuItem(
                    onClick = {
                        dropdownMenuExpanded = false
                        viewModel.setBaseCurrencyCode(it.code)
                    }
                ) {
                    CurrencySelectionItem(name = it.name, code = it.code)
                }
            }
        }
    }
}

@Composable
private fun CurrencySelectionItem(
    modifier: Modifier = Modifier,
    name: String,
    code: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 50.dp)
                .align(Alignment.CenterStart),
            text = name,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = "($code)",
            style = MaterialTheme.typography.subtitle1,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SortTypeSelection(
    filter: CurrenciesViewModel.Filter,
    viewModel: CurrenciesViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var dropdownMenuExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    dropdownMenuExpanded = !dropdownMenuExpanded
                }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 50.dp)
                    .align(Alignment.CenterStart),
                text = filter.sortType.uiName(),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = ""
            )
        }

        DropdownMenu(
            expanded = dropdownMenuExpanded,
            onDismissRequest = {
                dropdownMenuExpanded = false
            }
        ) {
            filter.availableSortTypes.forEach {
                DropdownMenuItem(
                    onClick = {
                        dropdownMenuExpanded = false
                        viewModel.setSortType(it)
                    }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it.uiName(),
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowOnlyFavoritesSwitch(
    filter: CurrenciesViewModel.Filter,
    viewModel: CurrenciesViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            modifier = Modifier.padding(2.dp),
            checked = filter.shouldShowOnlyFavorites,
            onCheckedChange = {
                viewModel.setShouldShowOnlyFavorites(!filter.shouldShowOnlyFavorites)
            }
        )
        ClickableText(
            modifier = Modifier.padding(8.dp),
            text = AnnotatedString("Show only favorites"),
            onClick = {
                viewModel.setShouldShowOnlyFavorites(!filter.shouldShowOnlyFavorites)
            }
        )
    }
}

private fun CurrenciesViewModel.SortType.uiName() = when (this) {
    CurrenciesViewModel.SortType.BY_ALPHABET -> "By alphabet"
    CurrenciesViewModel.SortType.BY_ALPHABET_DESC -> "By alphabet - descending"
    CurrenciesViewModel.SortType.BY_VALUE -> "By rate"
    CurrenciesViewModel.SortType.BY_VALUE_DESC -> "By rate - descending"
}

